package ci.example.app;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class Person {
  
	@PortableProperty(0)
 	public String name;
  
  	@PortableProperty(1)
  	public int age;
 
  	public Person() {}

  	public Person(String name, int age) {
		this.name = name;
    	this.age = age;
  	}

  	public String getName() { 
  		return this.name; 
  	}
 
	public int getAge()  { 
  		return this.age; 
	}
}
