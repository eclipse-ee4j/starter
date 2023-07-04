package org.eclipse.starter.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/version-info")
public class VersionInfo {

	public static final String VERSION_PROPERTY = "version";
	public static final String COMPILE_DEFAULT_ARCHETYPE_VERSION = "2.1.0";
	public static final String ARCHETYPE_VERSION_ENV_VAR = System.getenv("ARCHETYPE_VERSION"); 
	public static final String ARCHETYPE_VERSION = ( ARCHETYPE_VERSION_ENV_VAR != null)
			? System.getenv("ARCHETYPE_VERSION")
			: COMPILE_DEFAULT_ARCHETYPE_VERSION;
	
	Properties pomProperties = new Properties();
	
	public VersionInfo() throws IOException {
		loadPomProperties();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties() {
		Properties props = new Properties();
		props.setProperty("FINAL ARCHETYPE VERSION", ARCHETYPE_VERSION);
		props.setProperty("(compile-time default) archetype version", COMPILE_DEFAULT_ARCHETYPE_VERSION);
		props.setProperty("UI VERSION", pomProperties.getProperty(VERSION_PROPERTY));
        return Response.ok(props).build();
	}
	
	private void loadPomProperties() throws IOException {
    
        InputStream is = this.getClass().getClassLoader()
            .getResourceAsStream("version.properties");
        pomProperties.load(is);
    }

    
}

