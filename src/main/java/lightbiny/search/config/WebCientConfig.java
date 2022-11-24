package lightbiny.search.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class WebCientConfig {
	
	@Bean
	public ConnectionProvider defaultConnectionProvider() {
		return ConnectionProvider.builder("http-connection-pool")
				.maxConnections(50)
				.pendingAcquireTimeout(Duration.ofMillis(10))
				.pendingAcquireMaxCount(-1)
				.maxIdleTime(Duration.ofSeconds(5))
				.build();
	}
	
	@Bean
	public HttpClient defaultHttpcClient(ConnectionProvider connectionProvider) {
		return HttpClient.create(connectionProvider)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
				.responseTimeout(Duration.ofSeconds(1))
				.doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(3))
					.addHandlerLast(new WriteTimeoutHandler(5))
				);
	}
	
	@Bean
	public WebClient defaultWebClient(HttpClient httpClient) {
		return WebClient.builder()
				.clientConnector(new ReactorClientHttpConnector(httpClient ))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build()
				;
	}
}
