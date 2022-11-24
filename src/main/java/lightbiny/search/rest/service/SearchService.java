package lightbiny.search.rest.service;

import lightbiny.search.rest.dto.SearchDto;
import reactor.core.publisher.Mono;

public interface SearchService {
	Mono<SearchDto.Response> search(SearchDto.Request param);
}
