package org.eclipse.starter.ui;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
    // Otherwise empty, needed to enable Jakarta REST and specify path.
}
