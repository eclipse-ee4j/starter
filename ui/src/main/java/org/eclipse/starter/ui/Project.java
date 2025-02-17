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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());
    private static final Map<String, String> RUNTIMES = Map.ofEntries(
            entry("glassfish", "GlassFish"),
            entry("open-liberty", "Open Liberty"), entry("payara", "Payara"),
            entry("tomee", "TomEE"), entry("wildfly", "WildFly"));
    private static final String DEFAULT_GROUPID = "org.eclipse";
    private static final String DEFAULT_ARTIFACTID = "jakartaee-hello-world";

    private static Map<String, String> cache = new ConcurrentHashMap<>();

    @Inject
    private FacesContext facesContext;
    @Inject
    private ExternalContext externalContext;

    private Map<String, SelectItem> jakartaVersions = new LinkedHashMap<>();
    private double jakartaVersion = 11;

    private Map<String, SelectItem> profiles = new LinkedHashMap<>();
    private String profile = "core";

    private Map<String, SelectItem> javaVersions = new LinkedHashMap<>();
    private int javaVersion = 21;

    private Map<String, SelectItem> dockerFlags = new LinkedHashMap<>();
    private boolean docker = false;

    private Map<String, SelectItem> runtimes = new LinkedHashMap<>();
    private String runtime = "none";

    private String groupId = DEFAULT_GROUPID;
    private String artifactId = DEFAULT_ARTIFACTID;

    public Project() {
        jakartaVersions.put("11", new SelectItem("11", "Jakarta EE 11"));
        jakartaVersions.put("10", new SelectItem("10", "Jakarta EE 10"));
        jakartaVersions.put("9.1", new SelectItem("9.1", "Jakarta EE 9.1", "Jakarta EE 9.1", true));
        jakartaVersions.put("9", new SelectItem("9", "Jakarta EE 9", "Jakarta EE 9", true));
        jakartaVersions.put("8", new SelectItem("8", "Jakarta EE 8", "Jakarta EE 8", true));

        profiles.put("full", new SelectItem("full", "Platform", "Platform", true));
        profiles.put("web", new SelectItem("web", "Web Profile", "Web Profile", true));
        profiles.put("core", new SelectItem("core", "Core Profile"));

        javaVersions.put("21", new SelectItem("21", "Java SE 21"));
        javaVersions.put("17", new SelectItem("17", "Java SE 17"));
        javaVersions.put("11", new SelectItem("11", "Java SE 11", "Java SE 11", true));
        javaVersions.put("8", new SelectItem("8", "Java SE 8", "Java SE 8", true));

        dockerFlags.put("false", new SelectItem("false", "No"));
        dockerFlags.put("true", new SelectItem("true", "Yes", "Yes", true));

        runtimes.put("none", new SelectItem("none", "None"));

        List<String> shuffledRuntimes = new ArrayList<>(RUNTIMES.keySet());
        Collections.shuffle(shuffledRuntimes); // Ensure random order every time.
        shuffledRuntimes.forEach(r -> runtimes.put(r, new SelectItem(r, RUNTIMES.get(r))));

        // Disable all runtimes that do not support the EE 11 Core Profile.
        runtimes.get("glassfish").setDisabled(true);
        runtimes.get("payara").setDisabled(true);
        runtimes.get("tomee").setDisabled(true);
        runtimes.get("wildfly").setDisabled(true);
    }

    public Collection<SelectItem> getJakartaVersions() {
        return jakartaVersions.values();
    }

    public double getJakartaVersion() { return jakartaVersion; }

    public void setJakartaVersion(double jakartaVersion) {
        this.jakartaVersion = jakartaVersion;
    }

    public Collection<SelectItem> getProfiles() { return profiles.values(); }

    public String getProfile() { return profile; }

    public void setProfile(String profile) { this.profile = profile; }

    public Collection<SelectItem> getJavaVersions() {
        return javaVersions.values();
    }

    public int getJavaVersion() { return javaVersion; }

    public void setJavaVersion(int javaVersion) {
        this.javaVersion = javaVersion;
    }

    public Collection<SelectItem> getDockerFlags() {
        return dockerFlags.values();
    }

    public boolean isDocker() { return docker; }

    public void setDocker(boolean docker) { this.docker = docker; }

    public Collection<SelectItem> getRuntimes() { return runtimes.values(); }

    public String getRuntime() { return runtime; }

    public void setRuntime(String runtime) { this.runtime = runtime; }

    public String getGroupId() { return groupId; }

    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getArtifactId() { return artifactId; }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void onJakartaVersionChange() {
        LOGGER.log(Level.INFO,
            "Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
                new Object[] { jakartaVersion, profile, javaVersion, docker,
                runtime });
        if (!runtime.equals("tomee")) {
            profiles.get("full").setDisabled(false);
        }

        profiles.get("web").setDisabled(false);
        profiles.get("core").setDisabled(true);

        javaVersions.get("8").setDisabled(true);
        javaVersions.get("11").setDisabled(false);
        javaVersions.get("17").setDisabled(false);
        javaVersions.get("21").setDisabled(false);

        if (!docker && !profile.equals("core")) {
            runtimes.get("glassfish").setDisabled(false);
        }

        runtimes.get("payara").setDisabled(false);
        runtimes.get("tomee").setDisabled(true);
        runtimes.get("wildfly").setDisabled(false);

        if (jakartaVersion == 11) {
            profiles.get("full").setDisabled(true);
            profiles.get("web").setDisabled(true);
            profiles.get("core").setDisabled(false);

            javaVersions.get("11").setDisabled(true);

            runtimes.get("glassfish").setDisabled(true);
            runtimes.get("payara").setDisabled(true);
            runtimes.get("wildfly").setDisabled(true);
        } else if (jakartaVersion == 10) {
            if (!runtime.equals("glassfish")
                    && !runtime.equals("tomee")) {
                profiles.get("core").setDisabled(false);
            }
        } else {
            if ((jakartaVersion == 8)
                    || runtime.equals("none")
                    || runtime.equals("glassfish")
                    || runtime.equals("open-liberty")) {
                javaVersions.get("8").setDisabled(false);
            }

            if ((jakartaVersion == 8) && runtime.equals("glassfish")) {
                javaVersions.get("11").setDisabled(true);
                javaVersions.get("17").setDisabled(true);
                javaVersions.get("21").setDisabled(true);
            }

            if ((jakartaVersion != 8) && (javaVersion == 8)) {
                runtimes.get("glassfish").setDisabled(true);
            }

            if ((jakartaVersion == 9) || (jakartaVersion == 9.1)) {
                runtimes.get("payara").setDisabled(true);
                runtimes.get("wildfly").setDisabled(true);
            }

            if (profile.equals("web") &&
                    ((jakartaVersion == 8)
                    || ((jakartaVersion == 9.1) && (javaVersion != 8)))) {
                runtimes.get("tomee").setDisabled(false);
            }
        }
    }

    public void onProfileChange() {
        LOGGER.log(Level.INFO,
            "Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
                new Object[] { jakartaVersion, profile, javaVersion, docker,
                runtime });
        if (!(runtime.equals("glassfish") && (javaVersion > 8))) {
            jakartaVersions.get("8").setDisabled(false);
        }

        if (!(runtime.equals("payara") || runtime.equals("tomee")
                || runtime.equals("wildfly"))) {
            jakartaVersions.get("9").setDisabled(false);
        }

        if (!(runtime.equals("payara") || runtime.equals("wildfly")
                || (runtime.equals("tomee") && (javaVersion == 8)))) {
            jakartaVersions.get("9.1").setDisabled(false);
        }

        jakartaVersions.get("11").setDisabled(true);

        if (!docker && !((jakartaVersion == 8) && (javaVersion > 8)) 
                && (jakartaVersion != 11)) {
            runtimes.get("glassfish").setDisabled(false);
        }

        if ((jakartaVersion == 8) || ((jakartaVersion == 9.1) && (javaVersion != 8))) {
            runtimes.get("tomee").setDisabled(false);
        }

        if (profile.equals("core")) {
            jakartaVersions.get("8").setDisabled(true);
            jakartaVersions.get("9").setDisabled(true);
            jakartaVersions.get("9.1").setDisabled(true);

            if ((runtime.equals("none") || runtime.equals("open-liberty")) 
                    && (javaVersion > 11)) {
                jakartaVersions.get("11").setDisabled(false);
            }

            runtimes.get("glassfish").setDisabled(true);
            runtimes.get("tomee").setDisabled(true);
        } else if (profile.equals("full")) {
            runtimes.get("tomee").setDisabled(true);
        }
    }

    public void onJavaVersionChange() {
        LOGGER.log(Level.INFO,
            "Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
                new Object[] { jakartaVersion, profile, javaVersion, docker, 
                runtime });
        if (!runtime.equals("tomee")) {
            jakartaVersions.get("10").setDisabled(false);
        }

        jakartaVersions.get("11").setDisabled(true);

        if ((jakartaVersion > 9.1) && !runtime.equals("tomee")
                && !runtime.equals("glassfish")) {
            profiles.get("core").setDisabled(false);
        }

        if (!docker && !profile.equals("core") && (jakartaVersion != 11)) {
            runtimes.get("glassfish").setDisabled(false);
        }

        runtimes.get("tomee").setDisabled(true);

        if ((jakartaVersion != 9) && (jakartaVersion != 9.1) 
                && (jakartaVersion != 11)) {
            runtimes.get("payara").setDisabled(false);
            runtimes.get("wildfly").setDisabled(false);
        }

        if (javaVersion == 8) {
            jakartaVersions.get("10").setDisabled(true);
            profiles.get("core").setDisabled(true);
            
            if (profile.equals("web") && (jakartaVersion == 8)) {
                runtimes.get("tomee").setDisabled(false);
            }

            if (jakartaVersion != 8) {
                runtimes.get("payara").setDisabled(true);
                runtimes.get("wildfly").setDisabled(true);
            }
        } else {
            if ((javaVersion > 11) && profile.equals("core") 
                && (runtime.equals("open-liberty") || runtime.equals("none"))) {
                jakartaVersions.get("11").setDisabled(false);
            }

            if (jakartaVersion == 8) {
                runtimes.get("glassfish").setDisabled(true);
            }
    
            if (profile.equals("web") && ((jakartaVersion == 8) || (jakartaVersion == 9.1))) {
                runtimes.get("tomee").setDisabled(false);
            }
        }
    }

    public void onDockerChange() {
        LOGGER.log(Level.INFO,
            "Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
                new Object[] { jakartaVersion, profile, javaVersion, docker,
                runtime });

        if (docker) {
            runtimes.get("none").setDisabled(true);
            runtimes.get("glassfish").setDisabled(true);
        } else {
            runtimes.get("none").setDisabled(false);

            if (!profile.equals("core")
                    && !((jakartaVersion == 8) && (javaVersion > 8)) 
                    && (jakartaVersion != 11)) {
                runtimes.get("glassfish").setDisabled(false);
            }
        }
    }

    public void onRuntimeChange() {
        LOGGER.log(Level.INFO,
            "Validating form for Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}",
                new Object[] { jakartaVersion, profile, javaVersion, docker, runtime });
        if (!profile.equals("core")) {
            jakartaVersions.get("9").setDisabled(false);
            jakartaVersions.get("9.1").setDisabled(false);
        }

        if (javaVersion != 8) {
            jakartaVersions.get("10").setDisabled(false);
        }

        jakartaVersions.get("11").setDisabled(true);

        if (jakartaVersion > 9.1) {
            profiles.get("core").setDisabled(false);
        }

        profiles.get("full").setDisabled(false);

        if (jakartaVersion < 10) {
            javaVersions.get("8").setDisabled(false);
        }

        if (jakartaVersion < 11) {
            javaVersions.get("11").setDisabled(false);
        }

        javaVersions.get("17").setDisabled(false);
        javaVersions.get("21").setDisabled(false);

        dockerFlags.get("true").setDisabled(false);

        switch (runtime) {
            case "none":
                if (profile.equals("core") && (javaVersion > 11)) {
                    jakartaVersions.get("11").setDisabled(false);
                }

                dockerFlags.get("true").setDisabled(true);
                break;
            case "glassfish":
                profiles.get("core").setDisabled(true);

                if (jakartaVersion == 8) {
                    javaVersions.get("11").setDisabled(true);
                    javaVersions.get("17").setDisabled(true);
                    javaVersions.get("21").setDisabled(true);
                }

                dockerFlags.get("true").setDisabled(true);
                break;
            case "open-liberty":
                if (profile.equals("core") && (javaVersion > 11)) {
                    jakartaVersions.get("11").setDisabled(false);
                }
                break;
            case "payara":
                jakartaVersions.get("9").setDisabled(true);
                jakartaVersions.get("9.1").setDisabled(true);
                break;
            case "tomee":
                jakartaVersions.get("9").setDisabled(true);
                jakartaVersions.get("10").setDisabled(true);
                profiles.get("core").setDisabled(true);
                profiles.get("full").setDisabled(true);

                if (jakartaVersion != 8) {
                    javaVersions.get("8").setDisabled(true);
                }

                break;
            case "wildfly":
                jakartaVersions.get("9").setDisabled(true);
                jakartaVersions.get("9.1").setDisabled(true);
                break;
        }
    }

    public void generate() {
        try {
            LOGGER.log(Level.INFO,
                "Generating project - Jakarta EE version: {0}, Jakarta EE profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}, groupId: {5}, artifactId: {6}",
                    new Object[]{jakartaVersion, profile, javaVersion, docker, runtime, groupId, artifactId});

            String cachedDirectory = cache.get(getCacheKey());

            if ((cachedDirectory == null) || (!new File(cachedDirectory).exists())) {
                File workingDirectory = Files.createTempDirectory("starter-output-").toFile();
                LOGGER.log(Level.INFO, "Working directory: {0}", new Object[] { workingDirectory.getAbsolutePath() });

                LOGGER.info("Executing Maven Archetype.");
                Properties properties = new Properties();
                properties.putAll(Map.ofEntries(
                        entry("jakartaVersion",
                                ((jakartaVersion % 1.0 != 0) ? String.format("%s", jakartaVersion)
                                        : String.format("%.0f", jakartaVersion))),
                        entry("profile", profile),
                        entry("javaVersion", javaVersion),
                        entry("docker", (docker ? "yes" : "no")),
                        entry("runtime", runtime),
                        entry("groupId", groupId),
                        entry("artifactId", artifactId),
                        entry("package", groupId)));

                MavenUtility.invokeMavenArchetype("org.eclipse.starter", "jakarta-starter",
                        VersionInfo.ARCHETYPE_VERSION,
                        properties, workingDirectory);

                LOGGER.info("Creating zip file.");
                ZipUtility.zipDirectory(new File(workingDirectory, artifactId), workingDirectory);

                LOGGER.info("Downloading zip file.");
                downloadZip(new File(workingDirectory, artifactId + ".zip"));

                // Caching makes only sense if defaults weren't changed since otherwise it's unlikely to hit
                // cache.
                if (groupId.equals(DEFAULT_GROUPID) && artifactId.equals(DEFAULT_ARTIFACTID)) {
                    LOGGER.info("Caching output.");
                    cache.put(getCacheKey(), workingDirectory.getAbsolutePath());
                }

                workingDirectory.deleteOnExit();
            } else {
                LOGGER.log(Level.INFO, "Downloading zip file from cached directory: {0}",
                        new Object[] { cachedDirectory });
                downloadZip(new File(cachedDirectory, artifactId + ".zip"));
            }

            facesContext.responseComplete();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate zip.", e);
        }
    }

    private String getCacheKey() {
        return jakartaVersion + ":" + profile + ":" + javaVersion + ":"
                + docker + ":" + runtime + ":" + groupId + ":" + artifactId;
    }

    private void downloadZip(File zip) {
        try {
            // Some component library or filter might have set some headers in the
            // buffer beforehand. We want to get rid of them, else they may collide.
            externalContext.responseReset();
            externalContext.setResponseContentType("application/zip");
            externalContext.setResponseContentLength((int) zip.length());
            externalContext.setResponseHeader("Content-Disposition",
                    "attachment; filename=\"" + zip.getName() + "\"");

            Files.copy(zip.toPath(), externalContext.getResponseOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate zip download.", e);
        }
    }
}
