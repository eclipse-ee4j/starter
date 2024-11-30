package ${package}.jakarta.hello;

import ${eePackage}.ws.rs.core.Application;
import ${eePackage}.ws.rs.ApplicationPath;

@ApplicationPath("rest")
public class HelloApplication extends Application {
  // Needed to enable Jakarta REST and specify path.    
}
