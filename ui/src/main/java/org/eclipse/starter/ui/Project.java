package org.eclipse.starter.ui;

import static java.util.Map.entry;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

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
		LOGGER.info("Generating project");
		FacesContext context = FacesContext.getCurrentInstance();
		context.responseComplete();
	}
}