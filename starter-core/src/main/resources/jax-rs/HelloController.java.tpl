package [# th:text="${packageName}"/].jaxrs;

import [# th:text="${jakartaPackage}"/].ws.rs.GET;
import [# th:text="${jakartaPackage}"/].ws.rs.Path;

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
