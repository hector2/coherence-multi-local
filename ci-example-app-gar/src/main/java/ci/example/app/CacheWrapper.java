package ci.example.app;

import com.tangosol.net.CacheFactory;
import java.util.Set;

public class CacheWrapper {
	private static CacheWrapper INSTANCE;

	public Set getPeople() {
    	return CacheFactory.getCache("People").entrySet();
  	}

	public void addPerson(int personid, String name, int age) {
    	CacheFactory.getCache("People").put(personid, new Person(name, age));
  	}

  	public static final CacheWrapper getInstance() {
    	// TODO not a thread-safe class!
    	if (INSTANCE == null) 
    		INSTANCE = new CacheWrapper();
    	return INSTANCE;
	}
}
