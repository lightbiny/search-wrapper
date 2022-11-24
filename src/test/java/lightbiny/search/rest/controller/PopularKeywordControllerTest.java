package lightbiny.search.rest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import lightbiny.search.rest.service.Counter;
import lightbiny.search.rest.service.PopularKeywordService;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = PopularKeywordController.class)
public class PopularKeywordControllerTest {
	@Autowired
	WebTestClient webTestClient;
	
	@MockBean
	PopularKeywordService popularKeywordService;
	
	@MockBean
	Counter counter;
	
	@Test
	public void popularkeywords() {
		webTestClient.get()
			.uri("/v1/popularkeywords")
			.exchange()
			.expectStatus().isOk();
	}

}
