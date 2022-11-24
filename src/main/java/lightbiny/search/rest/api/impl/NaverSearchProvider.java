package lightbiny.search.rest.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
public class NaverSearchProvider implements SearchProvider {
	static final String HEADER_CLIENT_ID = "X-Naver-Client-Id";
	static final String HEADER_CLIENT_SECRET = "X-Naver-Client-Secret";
	
	private final WebClient webClient;
	private final String httpUrl;
	private final String clientId;
	private final String clientSecret;
	
	public NaverSearchProvider(WebClient webClient
			, @Value("${my.search-provider.naver.http-url}") String httpUrl
			, @Value("${my.search-provider.naver.client-id}") String clientId
			, @Value("${my.search-provider.naver.client-secret}") String clientSecret
			) {
		this.webClient = webClient;
		this.httpUrl = httpUrl;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@CircuitBreaker(name="local-search", fallbackMethod = "fallback")
	@Override
	public Mono<SearchDto.Response> search(SearchDto.Request param) {
		String uri = UriComponentsBuilder.fromHttpUrl(httpUrl)
				.queryParam("query", param.getQuery())
				.queryParam("start", param.getPage())
				.queryParam("display", param.getSize())
				.build()
				.toUriString();
				;

		 Mono<NaverResponse> result = webClient.get()
				.uri(uri)
				.header(HEADER_CLIENT_ID, clientId)
				.header(HEADER_CLIENT_SECRET, clientSecret)
				.retrieve()
				.bodyToMono(NaverResponse.class);
		 
		 return result.mapNotNull(s -> {
				 	SearchDto.Response response = SearchDto.Response.builder()
					 	.request(param)
					 	.documents(new ArrayList<>())
					 	.build();
		 
			 		s.getItems()
			 			.stream()
			 			.map(item -> 
			 				SearchDto.Document.builder()
			 					.name(item.getTitle())
			 					.url(item.getLink())
			 					.category(item.getCategory())
			 					.phone(item.getTelephone())
			 					.address(item.getAddress())
			 					.roadAddress(item.getRoadAddress())
			 					.build()
			 			)
			 			.forEach(response::addDocument)
			 			;
			 		
			 		return response;

		 		});
	}
	
	public Mono<SearchDto.Response> fallback(Throwable t) {
		log.warn("naver search error {}", t.getMessage());
		return Mono.just(SearchDto.Response.builder().error(true).build());
	}
	
	@Getter
	@Setter
	static class NaverResponse {
		private String lastBuildDate;
		private Integer total;
		private Integer start;
		private Integer display;
		private List<Item> items; 
		
	}
	
	@Getter
	@Setter
	static class Item {
		private String title;
		private String link;
		private String category;
		private String description;
		private String telephone;
		private String address;
		private String roadAddress;
		private Integer mapx;
		private Integer mapy;
	}
}
