package ci.example.app.servlets;

import ci.example.app.Person;
import ci.example.app.CacheWrapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

@WebServlet(name = "myservlet", urlPatterns = "myservlet")
public class MyServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
  			throws ServletException, IOException {
    	String id = request.getParameter("id");
    	String name = request.getParameter("name");
    	String age = request.getParameter("age");
    	if (name == null || name.isEmpty()
        	|| age == null || age.isEmpty()
        	|| id == null || id.isEmpty()) {
      		// no need to add a new entry
    	} else {
     		// we have a new entry - so add it
      		CacheWrapper.getInstance().addPerson(Integer.parseInt(id), name, Integer.parseInt(age));
    	}
    	renderPage(request, response);
  	}
  	
  	@Override
  	protected void doGet(HttpServletRequest request, HttpServletResponse response)
  			throws ServletException, IOException {
    	renderPage(request, response);
  	}
 
  	private void renderPage(HttpServletRequest request, HttpServletResponse response)
  			throws ServletException, IOException {
    	// get the data
    	Set people = CacheWrapper.getInstance().getPeople();
    	PrintWriter out = response.getWriter();
    	out.write("<html><head><title>MyServlet</title></head><body>");
	    out.write("<h2>Add a new person</h2>");
    	out.write("<form name=\"myform\" method=\"POST\">");
	    out.write("ID:<input type=\"text\" name=\"id\"/><br/>");
    	out.write("Name:<input type=\"text\" name=\"name\"/><br/>");
	    out.write("Age:<input type=\"text\" name=\"age\"/><br/>");
    	out.write("<input type=\"submit\" name=\"submit\" value=\"add\"/>");
	    out.write("</form>");
    	out.write("<h2>People in the cache now</h2>");
	    out.write("<table><tr><th>ID</th><th>Name</th><th>Age</th></tr>");
	    
	    // for each person in data
	    if (people != null) {
    		Iterator i = people.iterator();
		    while (i.hasNext()) {
        		Map.Entry entry = (Map.Entry)i.next();
        		out.write("<tr><td>"
					+ entry.getKey()
			        + "</td><td>"
			        + ((Person) entry.getValue()).getName()
          			+ "</td><td>"
          			+ ((Person) entry.getValue()).getAge()
          			+ "</td></tr>");
		    }
    	}
    	out.write("</table></body></html>");
	}
}
