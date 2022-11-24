package lightbiny.search.rest.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lightbiny.search.rest.api.SearchProvider;
import lightbiny.search.rest.api.SearchProviderFactory;
import lightbiny.search.rest.dto.SearchDto.Document;
import lightbiny.search.rest.dto.SearchDto.Request;
import lightbiny.search.rest.dto.SearchDto.Response;
import lightbiny.search.rest.service.SearchService;
import reactor.core.publisher.Mono;

@Service
public class SearchServiceImpl implements SearchService {
	private SearchProvider provider1;
	private SearchProvider provider2;
	
	public SearchServiceImpl(@Value("${my.search-provider.default}") String[] providers, SearchProviderFactory factory) {
		assert providers == null || providers.length < 2: "provider cannot be null";
		
		provider1 = factory.getSearchProvider(Optional.of(providers[0]));
		provider2 = factory.getSearchProvider(Optional.of(providers[1]));
	}

	@Override
	public Mono<Response> search(Request param) {
		Mono<Response> result1 = provider1.search(param);
		Mono<Response> result2 = provider2.search(param);
		
		return Mono.zip(result1, result2, (m1, m2) -> {
				List<Document> d1 = m1.getDocuments();
				List<Document> d2 = m2.getDocuments();
				
				List<Document> list = d1.stream()
					.filter(d -> d2.contains(d))
					.collect(Collectors.toList());
				
				list.addAll(d1.stream()
					.filter(d -> !d2.contains(d))
					.collect(Collectors.toList())
					);
				
				list.addAll(d2.stream()
					.filter(d -> !d1.contains(d))
					.collect(Collectors.toList())
					);
				
				return Response.builder().documents(list).build();
			});
	}

}
