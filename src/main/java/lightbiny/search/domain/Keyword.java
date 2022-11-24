package lightbiny.search.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "KEYWORD", indexes = {@Index(columnList = "COUNT DESC, MODIFIED_AT DESC")})
public class Keyword extends BaseTime {
	@Id
	@Column(name = "KEYWORD")
	private String keyword;
	
	@Column(name = "COUNT")
	private Integer count = 0;
	
	@Builder
	public Keyword(Long id, String keyword, Integer count) {
		this.keyword = keyword;
		this.count = count;
	}
	
	public void addCount(Integer count) {
		this.count += count;
	}
}
