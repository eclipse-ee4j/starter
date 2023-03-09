package org.eclipse.starter.ui;

import static java.util.Map.entry;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
	private static final Map<String, String> RUNTIMES = Map.ofEntries(entry("glassfish", "GlassFish"),
			entry("open-liberty", "Open Liberty"), entry("payara", "Payara"), entry("tomee", "TomEE"),
			entry("wildfly", "WildFly"));

	@Inject
	private FacesContext facesContext;
	@Inject
	private ExternalContext externalContext;

	private Map<String, SelectItem> jakartaVersions = new LinkedHashMap<String, SelectItem>();
	private double jakartaVersion = 10;

	private Map<String, SelectItem> profiles = new LinkedHashMap<String, SelectItem>();
	private String profile = "core";

	private Map<String, SelectItem> javaVersions = new LinkedHashMap<String, SelectItem>();
	private int javaVersion = 17;

	private Map<String, SelectItem> dockerOptions = new LinkedHashMap<String, SelectItem>();
	private boolean docker = false;

	private Map<String, SelectItem> runtimes = new LinkedHashMap<String, SelectItem>();
	private String runtime = "none";

	public Project() {
		jakartaVersions.put("8", new SelectItem("8", "Jakarta EE 8"));
		jakartaVersions.put("9", new SelectItem("9", "Jakarta EE 9"));
		jakartaVersions.put("9.1", new SelectItem("9.1", "Jakarta EE 9.1"));
		jakartaVersions.put("10", new SelectItem("10", "Jakarta EE 10"));

		profiles.put("core", new SelectItem("core", "Core Profile"));
		profiles.put("web", new SelectItem("web", "Web Profile"));
		profiles.put("full", new SelectItem("full", "Full platform"));

		javaVersions.put("8", new SelectItem("8", "Java SE 8", "Java SE 8", true));
		javaVersions.put("11", new SelectItem("11", "Java SE 11"));
		javaVersions.put("17", new SelectItem("17", "Java SE 17"));

		dockerOptions.put("false", new SelectItem("false", "No"));
		dockerOptions.put("true", new SelectItem("true", "Yes", "Yes", true));

		runtimes.put("none", new SelectItem("none", "None"));

		List<String> shuffledRuntimes = new ArrayList<>(RUNTIMES.keySet());
		Collections.shuffle(shuffledRuntimes);
		shuffledRuntimes.forEach(r -> runtimes.put(r, new SelectItem(r, RUNTIMES.get(r))));

		runtimes.get("tomee").setDisabled(true); // EE 10 is the default. TomEE needs to be disabled.
		runtimes.get("open-liberty").setDisabled(true); // EE 10 is the default. Open Liberty needs to be disabled.
		runtimes.get("glassfish").setDisabled(true); // Core is the default. GlassFish needs to be disabled.
	}

	public Collection<SelectItem> getJakartaVersions() {
		return jakartaVersions.values();
	}

	public double getJakartaVersion() {
		return jakartaVersion;
	}

	public void setJakartaVersion(double jakartaVersion) {
		this.jakartaVersion = jakartaVersion;
	}

	public Collection<SelectItem> getProfiles() {
		return profiles.values();
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public Collection<SelectItem> getJavaVersions() {
		return javaVersions.values();
	}

	public int getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(int javaVersion) {
		this.javaVersion = javaVersion;
	}

	public Collection<SelectItem> getDockerOptions() {
		return dockerOptions.values();
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

	public Collection<SelectItem> getRuntimes() {
		return runtimes.values();
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public void onJakartaVersionChange() {
		LOGGER.log(Level.INFO,
				"Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
				new Object[] { jakartaVersion, profile, javaVersion, docker, runtime });
		if (jakartaVersion != 10) {
			javaVersions.get("8").setDisabled(false);
			runtimes.get("tomee").setDisabled(false);
			runtimes.get("open-liberty").setDisabled(false);

			profiles.get("core").setDisabled(true);
			if (profile.equals("core")) {
				profile = "web";
				onProfileChange();
			}

			if ((jakartaVersion == 9) || (jakartaVersion == 9.1)) {
				runtimes.get("wildfly").setDisabled(true);
				if (runtime.equals("wildfly")) {
					runtime = "none";
				}
			} else {
				runtimes.get("wildfly").setDisabled(false);
			}
		} else {
			javaVersions.get("8").setDisabled(true);
			if (javaVersion == 8) {
				javaVersion = 11;
			}

			profiles.get("core").setDisabled(false);

			runtimes.get("tomee").setDisabled(true);
			if (runtime.equals("tomee")) {
				runtime = "none";
			}

			runtimes.get("open-liberty").setDisabled(true);
			if (runtime.equals("open-liberty")) {
				runtime = "none";
			}

			runtimes.get("wildfly").setDisabled(false);
		}
	}

	public void onProfileChange() {
		LOGGER.log(Level.INFO,
				"Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
				new Object[] { jakartaVersion, profile, javaVersion, docker, runtime });
		if (profile.equals("core")) {
			runtimes.get("glassfish").setDisabled(true);
			runtimes.get("tomee").setDisabled(true);

			if ((runtime.equals("glassfish")) || (runtime.equals("tomee"))) {
				runtime = "none";
			}
		} else if (profile.equals("full")) {
			runtimes.get("tomee").setDisabled(true);
			runtimes.get("glassfish").setDisabled(false);

			if (runtime.equals("tomee")) {
				runtime = "none";
			}
		} else {
			runtimes.get("tomee").setDisabled(false);
			runtimes.get("glassfish").setDisabled(false);
		}
	}

	public void onJavaVersionChange() {
		LOGGER.log(Level.INFO,
				"Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
				new Object[] { jakartaVersion, profile, javaVersion, docker, runtime });
		if (javaVersion == 8) {
			jakartaVersions.get("10").setDisabled(true);
			if (jakartaVersion == 10) {
				jakartaVersion = 9.1;
			}

			profiles.get("core").setDisabled(true);
			if (profile.equals("core")) {
				profile = "web";
			}
		} else {
			jakartaVersions.get("10").setDisabled(false);
		}
	}

	public void onDockerChange() {
		LOGGER.log(Level.INFO,
				"Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
				new Object[] { jakartaVersion, profile, javaVersion, docker, runtime });
		if (docker) {
			runtimes.get("none").setDisabled(true);
			runtimes.get("glassfish").setDisabled(true);

			Iterator<String> iterator = runtimes.keySet().iterator();
			while (runtime.equals("none") || runtime.equals("glassfish")) {
				runtime = iterator.next();
			}
		} else {
			runtimes.get("none").setDisabled(false);
			runtimes.get("glassfish").setDisabled(false);
		}
	}

	public void onRuntimeChange() {
		LOGGER.log(Level.INFO,
				"Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
				new Object[] { jakartaVersion, profile, javaVersion, docker, runtime });
		dockerOptions.get("true").setDisabled(false);

		if (runtime.equals("none")) {
			dockerOptions.get("true").setDisabled(true);
			docker = false;
		} else if (runtime.equals("payara")) {
			if ((jakartaVersion != 8) && (javaVersion == 8)) {
				javaVersion = 11;
			}
		} else if (runtime.equals("glassfish")) {
			dockerOptions.get("true").setDisabled(true);
			docker = false;

			if ((jakartaVersion != 8) && (javaVersion == 8)) {
				javaVersion = 11;
			}
		} else if (runtime.equals("tomee")) {
			if ((jakartaVersion != 8) && (javaVersion == 8)) {
				javaVersion = 11;
			}
		}
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