package lightbiny.search.rest.service;

import java.util.List;

import lightbiny.search.rest.dto.PopularKeywordDto;

public interface PopularKeywordService {
	List<PopularKeywordDto> getPopularKeyword();
}
