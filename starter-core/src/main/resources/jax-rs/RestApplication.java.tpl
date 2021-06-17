package [# th:text="${packageName}"/].jaxrs;

[# th:switch="${jakartaVersion}"]
[# th:case="'8.0.0'"]
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
[/]
[# th:case="'9.1.0'"]
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
[/]
[/]

/**
 *
 */
@ApplicationPath("/api")
public class [# th:text="${#strings.capitalize(projectName)}"/]RestApplication extends Application {
}