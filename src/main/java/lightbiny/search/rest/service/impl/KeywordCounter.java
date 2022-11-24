package lightbiny.search.rest.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lightbiny.search.domain.Keyword;
import lightbiny.search.rest.repository.KeywordRepository;
import lightbiny.search.rest.service.Counter;

@Service
public class KeywordCounter implements Counter {
	private final Map<String, AtomicInteger> first;
	private final Map<String, AtomicInteger> second;
	private Map<String, AtomicInteger> current;
	
	private final KeywordRepository keywordRepository;
	
	public KeywordCounter(KeywordRepository keywordRepository) {
		this.first = Collections.synchronizedMap(new HashMap<>());
		this.second = Collections.synchronizedMap(new HashMap<>());
		this.current = this.first;
		
		this.keywordRepository = keywordRepository;
	}

	@Override
	public int increase(String key) {
		return current.merge(key, new AtomicInteger(1), (o, n) -> {o.incrementAndGet(); return o;}).get();
	}

	@Scheduled(fixedDelay = 3000)
	@Transactional
	synchronized public void flush() {
		Map<String, AtomicInteger> counter;
		
		if (current == first) {
			current = second;
			counter = first;
		} else {
			current = first;
			counter = second;
		}
		
		counter.forEach((k, v) -> {
			Optional<Keyword> find = keywordRepository.findByKeyword(k);
			if (find.isPresent()) {
				keywordRepository.updateKeywordCount(k, v.get());
			} else {
				Keyword keyword = Keyword.builder().keyword(k).count(v.get()).build();
				keywordRepository.save(keyword);
			}
		});

		counter.clear();
		
	}
}
