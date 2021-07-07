package [# th:text="${packageName}"/].jaxrs;

import [# th:text="${jakartaPackage}"/].ws.rs.ApplicationPath;
import [# th:text="${jakartaPackage}"/].ws.rs.core.Application;

/**
 *
 */
@ApplicationPath("/api")
public class [# th:text="${#strings.capitalize(projectName)}"/]RestApplication extends Application {
}