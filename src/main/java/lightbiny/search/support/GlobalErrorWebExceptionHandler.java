package lightbiny.search.support;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

	public GlobalErrorWebExceptionHandler(ErrorAttributes errorAttributes
			, WebProperties webProperties
			, ApplicationContext applicationContext
			, ServerCodecConfigurer configurer
			) {
		super(errorAttributes, webProperties.getResources(), applicationContext);
		this.setMessageWriters(configurer.getWriters());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);	
    }

	private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
		Throwable t = getError(request);
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		if (t instanceof ResponseStatusException ex) {
			status = ex.getStatus();
		}
		
		Map<String, Object> errorPropertiesMap = getErrorAttributes(request, ErrorAttributeOptions.defaults());

		return ServerResponse.status(status)
    	         .contentType(MediaType.APPLICATION_JSON)
    	         .body(BodyInserters.fromValue(errorPropertiesMap))
    	         ;
	}
}
