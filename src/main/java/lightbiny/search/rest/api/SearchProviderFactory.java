package lightbiny.search.rest.api;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SearchProviderFactory {
	private final String defaultName;
	private final String defaultSearchPrivider;
	private final Map<String, SearchProvider> providers;
	
	public SearchProviderFactory(@Value("${my.search-provider.default}") String defaultName, ApplicationContext context) {
		this.defaultName = defaultName;
		this.defaultSearchPrivider = defaultName + "SearchProvider";
		this.providers = context.getBeansOfType(SearchProvider.class);
	}
	
	public SearchProvider getSearchProvider(Optional<String> name) {
		String providerName = name.orElse(defaultName) + "SearchProvider";
		
		if (providers.containsKey(providerName)) {
			log.debug("{} selected", providerName);
			return providers.get(providerName);
		} else {
			log.debug("{} does not exists. {} selected", providerName, defaultSearchPrivider);
			return providers.get(defaultSearchPrivider);
		}
	}

}
