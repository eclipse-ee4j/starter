package ${package}.jakarta.hello;

import ${eePackage}.ws.rs.GET;
import ${eePackage}.ws.rs.Path;
import ${eePackage}.ws.rs.PathParam;
import ${eePackage}.ws.rs.Produces;
import ${eePackage}.ws.rs.core.MediaType;

@Path("hello")
public class HelloWorldResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Hello hello(@PathParam("name") String name) {
		if ((name == null) || name.trim().isEmpty())  {
			name = "world";
		}

		return new Hello(name);
	}
}