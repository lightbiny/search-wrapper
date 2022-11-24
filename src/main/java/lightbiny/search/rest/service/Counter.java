package lightbiny.search.rest.service;

public interface Counter {
	int increase(String key);
	
	void flush();
}
