package org.eclipse.starter.ui;

import static java.util.Map.entry;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class Project {

	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	private static final Map<String, String> RUNTIMES = Map.ofEntries(entry("GlassFish", "glassfish"),
			entry("Open Liberty", "open-liberty"), entry("Payara", "payara"), entry("TomEE", "tomee"),
			entry("WildFly", "wildfly"));

	@Inject
	private FacesContext facesContext;
	@Inject
	private ExternalContext externalContext;

	private double jakartaVersion = 10;
	private String profile = "core";
	private int javaVersion = 17;
	private boolean docker = false;
	private String runtime = "none";

	public double getJakartaVersion() {
		return jakartaVersion;
	}

	public void setJakartaVersion(double jakartaVersion) {
		this.jakartaVersion = jakartaVersion;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public int getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(int javaVersion) {
		this.javaVersion = javaVersion;
	}

	public boolean isDocker() {
		return docker;
	}

	public void setDocker(boolean docker) {
		this.docker = docker;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public Map<String, String> getRuntimes() {
		List<String> runtimes = new ArrayList<>(RUNTIMES.keySet());
		Collections.shuffle(runtimes);

		Map<String, String> shuffledRuntimes = new LinkedHashMap<>();
		runtimes.forEach(r -> shuffledRuntimes.put(r, RUNTIMES.get(r)));

		return shuffledRuntimes;
	}

	public void generate() {
		try {
			LOGGER.log(Level.INFO,
					"Generating project - Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
					new Object[] { jakartaVersion, profile, javaVersion, docker, runtime });

			File workingDirectory = Files.createTempDirectory("starter-output-").toFile();
			LOGGER.log(Level.INFO, "Working directory: {0}", new Object[] { workingDirectory.getAbsolutePath() });

			
			LOGGER.info("Executing Maven Archetype.");
			Properties properties = new Properties();
			properties.putAll(Map.ofEntries(
					entry("jakartaVersion",
							((jakartaVersion % 1.0 != 0) ? String.format("%s", jakartaVersion)
									: String.format("%.0f", jakartaVersion))),
					entry("profile", profile), entry("javaVersion", javaVersion),
					entry("docker", (docker ? "yes" : "no")), entry("runtime", runtime)));
			MavenUtility.invokeMavenArchetype("org.eclipse.starter", "jakarta-starter", "2.0-SNAPSHOT", properties,
					workingDirectory);

			LOGGER.info("Creating zip file.");
			ZipUtility.zipDirectory(new File(workingDirectory, "jakartaee-hello-world"), workingDirectory);

			LOGGER.info("Downloading zip file.");
			downloadZip(new File(workingDirectory, "jakartaee-hello-world.zip"));

			LOGGER.info("Deleting working directory.");
			workingDirectory.delete();

			facesContext.responseComplete();
		} catch (IOException e) {
			throw new RuntimeException("Failed to generate zip.", e);
		}
	}

	private void downloadZip(File zip) {
		try {
			// Some component library or filter might have set some headers in the
			// buffer beforehand. We want to get rid of them, else they may collide.
			externalContext.responseReset();
			externalContext.setResponseContentType("application/zip");
			externalContext.setResponseContentLength((int) zip.length());
			externalContext.setResponseHeader("Content-Disposition",
					"attachment; filename=\"jakartaee-hello-world.zip\"");

			Files.copy(zip.toPath(), externalContext.getResponseOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Failed to generate zip download.", e);
		}
	}
}