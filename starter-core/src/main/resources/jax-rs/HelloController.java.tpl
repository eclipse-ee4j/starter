package [# th:text="${packageName}"/].jaxrs;

[# th:switch="${jakartaVersion}"]
[# th:case="'8.0.0'"]
import javax.ws.rs.GET;
import javax.ws.rs.Path;
[/]
[# th:case="'9.1.0'"]
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
[/]
[/]

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
