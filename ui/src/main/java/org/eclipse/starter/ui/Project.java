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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.maven.cli.MavenCli;

@Named
@RequestScoped
public class Project {

	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	private static final Map<String, String> RUNTIMES = Map.ofEntries(entry("GlassFish", "glassfish"),
			entry("Open Liberty", "open-liberty"), entry("Payara", "payara"), entry("TomEE", "tomee"),
			entry("WildFly", "wildfly"));

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
		LOGGER.log(Level.INFO,
				"Generating project - Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
				new Object[] { jakartaVersion, profile, javaVersion, docker, runtime });

		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();

			File workingDirectory = Files.createTempDirectory("starter-output-").toFile();
			LOGGER.log(Level.INFO, "Working directory: {0}", new Object[] { workingDirectory.getAbsolutePath() });

			invokeMavenArchetype(workingDirectory);
			ZipUtility.zipDirectory(new File(workingDirectory, "jakartaee-hello-world"), workingDirectory);
			downloadZip(new File(workingDirectory, "jakartaee-hello-world.zip"), facesContext.getExternalContext());

			workingDirectory.delete();

			facesContext.responseComplete();
		} catch (IOException e) {
			throw new RuntimeException("Failed to generate zip.", e);
		}
	}

	private void invokeMavenArchetype(File workingDirectory) {
		System.setProperty(MavenCli.MULTIMODULE_PROJECT_DIRECTORY, workingDirectory.getAbsolutePath());

		String[] options = { "archetype:generate", "-DinteractiveMode=false", "-DaskForDefaultPropertyValues=false",
				"-DarchetypeGroupId=org.eclipse.starter", "-DarchetypeArtifactId=jakarta-starter",
				"-DarchetypeVersion=2.0-SNAPSHOT",
				"-DjakartaVersion=" + ((jakartaVersion % 1.0 != 0) ? String.format("%s", jakartaVersion)
						: String.format("%.0f", jakartaVersion)),
				"-Dprofile=" + profile, "-DjavaVersion=" + javaVersion, "-Ddocker=" + (docker ? "yes" : "no"),
				"-Druntime=" + runtime };
		int result = new MavenCli().doMain(options, workingDirectory.getAbsolutePath(), System.out, System.err);

		if (result != 0) {
			throw new RuntimeException("Failed to invoke Maven Archetype.");
		}
	}

	private void downloadZip(File zip, ExternalContext externalContext) {
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