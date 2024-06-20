package org.eclipse.starter.ui;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
   // Otherwise empty, needed to enable Jakarta REST and specify path.
}
