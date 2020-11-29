package [# th:text="${packageName}"/].jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 */
@Path("/hello")
public class HelloController {

    @GET
    public String hello() {
        return "Hello World";
    }
}
