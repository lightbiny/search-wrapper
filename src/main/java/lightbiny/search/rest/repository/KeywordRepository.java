package lightbiny.search.rest.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lightbiny.search.domain.Keyword;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	Optional<Keyword> findByKeyword(String keyword);
	
	Page<Keyword> findAll(Pageable pageable);

	@Modifying
	@Query(value = "UPDATE KEYWORD SET COUNT = COUNT + :count WHERE KEYWORD = :keyword", nativeQuery = true)
	int updateKeywordCount(@Param("keyword") String keyword, @Param("count") int count);

}
