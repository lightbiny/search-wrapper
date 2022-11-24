package lightbiny.search.rest.service.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import lightbiny.search.domain.Keyword;
import lightbiny.search.rest.dto.PopularKeywordDto;
import lightbiny.search.rest.repository.KeywordRepository;
import lightbiny.search.rest.service.PopularKeywordService;

@Service
public class PopularKeywordServiceimpl implements PopularKeywordService {

	private final KeywordRepository keywordRepository;

	public PopularKeywordServiceimpl(KeywordRepository popularKeywordRepository) {
		this.keywordRepository = popularKeywordRepository;
	}
	
	@Cacheable(value = "popular_keyword")
	@Override
	public List<PopularKeywordDto> getPopularKeyword() {
		Page<Keyword> result = keywordRepository.findAll(PageRequest.of(0, 10, Sort.by(Order.desc("count"), Order.desc("modifiedAt"))));
		
		return result.stream()
					.map(p -> PopularKeywordDto.builder().keyword(p.getKeyword()).count(p.getCount()).build())
					.toList();
	}

}
