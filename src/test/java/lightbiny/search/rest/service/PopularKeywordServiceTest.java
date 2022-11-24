package lightbiny.search.rest.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import lightbiny.search.rest.dto.PopularKeywordDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PopularKeywordServiceTest {
	@Autowired
	Counter counter;
	
	@Autowired
	PopularKeywordService service;
	
	ExecutorService executor = Executors.newCachedThreadPool();
	
	@Test
	public void keyword_count_test() throws InterruptedException {
		for (int i=0; i<100; i++) {
			executor.submit(() -> counter.increase("검색어 " + (int)(Math.random()*10)));
		}
		Thread.sleep(5000);
		
		List<PopularKeywordDto> keyword =  service.getPopularKeyword();
		log.debug("popular keyword {}", keyword);

		assertThat(keyword.size(), is(10));
		int sum = keyword.stream()
			.mapToInt(k -> k.getCount())
			.sum();
		assertThat(sum, is(100));
		
	}
	
}
