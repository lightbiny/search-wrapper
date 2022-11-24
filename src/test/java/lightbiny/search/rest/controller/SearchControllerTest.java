package lightbiny.search.rest.controller;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import lightbiny.search.rest.api.SearchProviderFactory;
import lightbiny.search.rest.api.impl.KakaoSearchProvider;
import lightbiny.search.rest.api.impl.NaverSearchProvider;
import lightbiny.search.rest.dto.SearchDto;
import lightbiny.search.rest.service.Counter;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = SearchController.class)
public class SearchControllerTest {
	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	SearchProviderFactory searchProviderFactory;
	
	@MockBean
	KakaoSearchProvider kakaoSearchProvider;
	
	@MockBean
	NaverSearchProvider naverSearchProvider;
	
	@MockBean
	Counter counter;
	
	@Test
	public void kakao_search() {
		BDDMockito.given(searchProviderFactory.getSearchProvider(Optional.of("kakao"))).willReturn(kakaoSearchProvider);
		webTestClient.get()
			.uri("/v1/search?query=맛집")
			.exchange()
			.expectStatus().isOk();
	}

	@Test
	public void naver_search() {
		BDDMockito.given(searchProviderFactory.getSearchProvider(Optional.of("kakao"))).willReturn(naverSearchProvider);
		webTestClient.get()
			.uri("/v1/search?query=맛집")
			.exchange()
			.expectStatus().isOk()
			.expectBody(SearchDto.Response.class)
			;
	}

}
