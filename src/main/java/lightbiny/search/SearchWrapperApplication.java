package lightbiny.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "lightbiny.search")
public class SearchWrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchWrapperApplication.class, args);
	}

}
