package lightbiny.search.rest.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lightbiny.search.rest.dto.SearchDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SearchProviderTest {
	@Autowired
	SearchProviderFactory searchProviderFactory;
	
	boolean isDone = false;
	
	@Test
	public void kakaoTest() throws InterruptedException {
		SearchDto.Request param = SearchDto.Request.builder()
				.query("은행")
				.build();
		
		SearchDto.Response result = searchProviderFactory.getSearchProvider(Optional.of("kakao")).search(param).block();
		log.debug("search result {}", result);
		
		checkResponse(param, result);
	}
	
	private void checkResponse(SearchDto.Request request, SearchDto.Response response) {
		assertThat(response.getRequest().getQuery(), is(request.getQuery()));
		assertThat(response.getRequest().getPage(), is(request.getPage()));
		assertThat(response.getRequest().getSize(), is(request.getSize()));
		assertThat(response.getDocuments(), isA(List.class));
		assertThat(response.getDocuments().size(), greaterThan(0));
		assertThat(response.getDocuments().get(0), isA(SearchDto.Document.class));
		assertThat(response.getDocuments().get(0).getName(), isA(String.class));
		assertThat(response.getDocuments().get(0).getUrl(), isA(String.class));
		assertThat(response.getDocuments().get(0).getCategory(), isA(String.class));
		assertThat(response.getDocuments().get(0).getPhone(), isA(String.class));
		assertThat(response.getDocuments().get(0).getAddress(), isA(String.class));
		assertThat(response.getDocuments().get(0).getRoadAddress(), isA(String.class));
	}


	@Test
	public void naverTest() throws InterruptedException {
		SearchDto.Request param = SearchDto.Request.builder()
				.query("은행")
				.build();
		
		SearchDto.Response result = searchProviderFactory.getSearchProvider(Optional.of("naver")).search(param).block();
		log.debug("search result {}", result);
		
		checkResponse(param, result);
	}
}
