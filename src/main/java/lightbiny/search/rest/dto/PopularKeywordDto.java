package lightbiny.search.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PopularKeywordDto {
	private String keyword;
	private Integer count;
	
	@Builder
	public PopularKeywordDto(String keyword, Integer count) {
		this.keyword = keyword;
		this.count = count;
	}
}
