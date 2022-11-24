package lightbiny.search.rest.api;

import lightbiny.search.rest.dto.SearchDto;
import reactor.core.publisher.Mono;

public interface SearchProvider {
	Mono<SearchDto.Response> search(SearchDto.Request param);
}
