package lightbiny.search.rest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lightbiny.search.rest.dto.PopularKeywordDto;
import lightbiny.search.rest.service.PopularKeywordService;

@RestController
public class PopularKeywordController {
	
	private final PopularKeywordService popularKeywordService;
	
	public PopularKeywordController(PopularKeywordService popularKeywordService) {
		this.popularKeywordService = popularKeywordService;
	}

	@GetMapping("/v1/popularkeywords")
	public List<PopularKeywordDto> popularkeywords() {
		return popularKeywordService.getPopularKeyword();
	}
	
}
