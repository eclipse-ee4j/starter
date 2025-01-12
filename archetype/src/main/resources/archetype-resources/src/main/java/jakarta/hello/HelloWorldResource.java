package ${package}.jakarta.hello;

import ${eePackage}.ws.rs.GET;
import ${eePackage}.ws.rs.Path;
import ${eePackage}.ws.rs.Produces;
import ${eePackage}.ws.rs.QueryParam;
import ${eePackage}.ws.rs.core.MediaType;

@Path("hello")
public class HelloWorldResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public Hello hello(@QueryParam("name") String name) {
        if ((name == null) || name.trim().isEmpty())  {
            name = "world";
        }

        return new Hello(name);
    }
}
