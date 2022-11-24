package lightbiny.search.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lightbiny.search.rest.service.Counter;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

@Component
public class PostSearchFilter implements WebFilter {
	
	private Counter counter;
	
	public PostSearchFilter(Counter counter) {
		this.counter = counter;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange).doFinally(signalType -> {
			String path = exchange.getRequest().getURI().getPath();
			if (path.endsWith("/search") && SignalType.ON_COMPLETE == signalType) {
				String query = exchange.getRequest().getQueryParams().getFirst("query");
				counter.increase(query);
			}
		});
	}

}
