package lightbiny.search.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lightbiny.search.rest.dto.SearchDto;
import lightbiny.search.rest.service.SearchService;
import reactor.core.publisher.Mono;

@RestController
public class SearchController {
	
	private final SearchService searchService;
	
	@Autowired
	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}
	
	@GetMapping("/v1/search")
	public Mono<SearchDto.Response> search(
			@RequestParam(name="query", required = true) String query
			) {
		
		if (!StringUtils.hasText(query)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "query required");
		}
		
		return searchService.search(
					SearchDto.Request.builder()
						.query(query)
						.build()
				);
	}
}
