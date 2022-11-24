package lightbiny.search.rest.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lightbiny.search.rest.api.SearchProvider;
import lightbiny.search.rest.dto.SearchDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class KakaoSearchProvider implements SearchProvider {
	private final WebClient webClient;
	private final String httpUrl;
	private final String apiKey;
	
	public KakaoSearchProvider(WebClient webClient
			, @Value("${my.search-provider.kakao.http-url}") String httpUrl
			, @Value("${my.search-provider.kakao.api-key}") String apiKey
			) {
		this.webClient = webClient;
		this.httpUrl = httpUrl;
		this.apiKey = apiKey;
	}

	@CircuitBreaker(name="local-search", fallbackMethod = "fallback")
	@Override
	public Mono<SearchDto.Response> search(SearchDto.Request param) {
		String uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
				.queryParam("query", param.getQuery())
				.queryParam("page", param.getPage())
				.queryParam("size", param.getSize())
				.build()
				.toUriString();
				;

		 Mono<KakaoResponse> result = webClient.get()
				.uri(uri)
				.header(HttpHeaders.AUTHORIZATION, apiKey)
				.retrieve()
				.bodyToMono(KakaoResponse.class);
		 
		 return result.mapNotNull(s -> {
				 	SearchDto.Response response = SearchDto.Response.builder()
					 	.request(param)
					 	.documents(new ArrayList<>())
					 	.build();
		 
			 		s.getDocuments()
			 			.stream()
			 			.map(d -> 
			 				SearchDto.Document.builder()
			 					.name(d.getPlace_name())
			 					.url(d.getPlace_url())
			 					.category(d.getCategory_name())
			 					.phone(d.getPhone())
			 					.address(d.getAddress_name())
			 					.roadAddress(d.getRoad_address_name())
			 					.build()
			 			)
			 			.forEach(response::addDocument)
			 			;
			 		
			 		return response;

		 		});
	}
	
	public Mono<SearchDto.Response> fallback(Throwable t) {
		log.warn("kakao search error {}", t.getMessage());
		return Mono.just(SearchDto.Response.builder().error(true).build());
	}
	
	@Getter
	@Setter
	static class KakaoResponse {
		private Meta meta;
		private List<Document> documents;
	}
	
	@Getter
	@Setter
	static class Meta {
		private Integer total_count;
		private Integer pageable_count;
		private Boolean is_end;
		private RegionInfo same_name;
	}
	
	@Getter
	@Setter
	static class RegionInfo {
		private String[] region;
		private String keyword;
		private String selected_region;
	}
	
	@Getter
	@Setter
	static class Document {
		private String id;
		private String place_name;
		private String category_name;
		private String category_group_code;
		private String category_group_name;
		private String phone;
		private String address_name;
		private String road_address_name;
		private String x;
		private String y;
		private String place_url;
		private String distance;
	}
}
