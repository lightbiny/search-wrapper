package lightbiny.search.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();

        List<CaffeineCache> caches = new ArrayList<>();
        for (Caches c : Caches.values()) {
            caches.add(new CaffeineCache(c.getValue(),
                    Caffeine.newBuilder().recordStats().expireAfterAccess(c.getTtl(), TimeUnit.SECONDS)
                            .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                            .maximumSize(c.getMaxSize()) // entry 갯수
                            .build())
            );
        }
        manager.setCaches(caches);
        return manager;
    }
    
    public enum Caches {
    	POPULAR_KEYWORD("popular_keyword", 5, 3000);
    	
    	private String value;
    	private int ttl = 5;
    	private int maxSize = 1000;
    	
    	Caches(String value, int ttl, int maxSize) {
    		this.value = value;
    		this.ttl = ttl;
    		this.maxSize = maxSize;
    	}
    	
    	public String getValue() {
    		return this.value;
    	}
    	
    	public int getTtl() {
    		return this.ttl;
    	}
    	
    	public int getMaxSize() { 
    		return this.maxSize;
    	}
    }
}
