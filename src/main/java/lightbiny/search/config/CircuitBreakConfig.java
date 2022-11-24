package lightbiny.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Configuration
public class CircuitBreakConfig {

	@Bean
	public CircuitBreakerRegistry circuitBreakerRegistry() {
		return CircuitBreakerRegistry.of(CircuitBreakerConfig.custom()
					.slidingWindowSize(10)
					.failureRateThreshold(80)
					.build()
				);
	}
	
	@Bean
	public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
		return circuitBreakerRegistry.circuitBreaker("local-search");
	}
}
