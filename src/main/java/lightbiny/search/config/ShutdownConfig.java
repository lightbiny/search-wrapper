package lightbiny.search.config;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lightbiny.search.rest.service.Counter;

@Configuration
public class ShutdownConfig {
	
	@Bean
	public ShutdownBean shutdownBean(Counter counter) {
		return new ShutdownBean(counter);
	}
	
	
	
	public static class ShutdownBean {
		private Counter counter;
		
		public ShutdownBean(Counter counter) {
			this.counter = counter;
		}
		
		@PreDestroy
		public void onDestory() throws Exception {
			counter.flush();
		}
	}
}
