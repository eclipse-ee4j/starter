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

@Named @ViewScoped
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger
        .getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final Map<String, String> RUNTIMES = Map.ofEntries(
        entry("glassfish", "GlassFish"),
        entry("open-liberty", "Open Liberty"),
        entry("payara", "Payara"),
        entry("tomee", "TomEE"),
        entry("wildfly", "WildFly")
    );
    private static final String DEFAULT_GROUPID = "org.eclipse";
    private static final String DEFAULT_ARTIFACTID = "jakartaee-hello-world";

    private static final String DEFAULT_ARCHETYPE_VERSION = "2.8.0";
    private static final String ARCHETYPE_VERSION = (System.getenv("ARCHETYPE_VERSION") != null)
            ? System.getenv("ARCHETYPE_VERSION")
            : DEFAULT_ARCHETYPE_VERSION;

    private static Map<String, String> cache = new ConcurrentHashMap<>();

    @Inject
    private FacesContext facesContext;
    @Inject
    private ExternalContext externalContext;

    private Map<Double, SelectItem> jakartaVersions = new LinkedHashMap<>();
    private double jakartaVersion = 0.0;

    private boolean profileDisabled = true;
    private Map<String, SelectItem> profiles = new LinkedHashMap<>();
    private String profile = "";

    private boolean javaVersionDisabled = true;
    private Map<Integer, SelectItem> javaVersions = new LinkedHashMap<>();
    private int javaVersion = 0;

    private boolean dockerDisabled = true;
    private Map<Boolean, SelectItem> dockerFlags = new LinkedHashMap<>();
    private boolean docker = false;

    private boolean runtimeDisabled = true;
    private Map<String, SelectItem> runtimes = new LinkedHashMap<>();
    private String runtime = "";

    private String groupId = DEFAULT_GROUPID;
    private String artifactId = DEFAULT_ARTIFACTID;
    private boolean generateDisabled = true;

    public Project() {
        jakartaVersions.put(0.0, new SelectItem(0.0, "-- Select Jakarta EE version --", null, true));
        jakartaVersions.put(11.0, new SelectItem(11.0, "Jakarta EE 11"));
        jakartaVersions.put(10.0, new SelectItem(10.0, "Jakarta EE 10"));
        jakartaVersions.put(9.1, new SelectItem(9.1, "Jakarta EE 9.1"));
        jakartaVersions.put(9.0, new SelectItem(9.0, "Jakarta EE 9"));
        jakartaVersions.put(8.0, new SelectItem(8.0, "Jakarta EE 8"));

        profiles.put("", new SelectItem("", "-- Select Jakarta EE Profile --", null, true));
        profiles.put("full", new SelectItem("full", "Platform"));
        profiles.put("web", new SelectItem("web", "Web Profile"));
        profiles.put("core", new SelectItem("core", "Core Profile"));

        javaVersions.put(0, new SelectItem(0, "-- Select Java SE version --", null, true));
        javaVersions.put(21, new SelectItem(21, "Java SE 21"));
        javaVersions.put(17, new SelectItem(17, "Java SE 17"));
        javaVersions.put(11, new SelectItem(11, "Java SE 11"));
        javaVersions.put(8, new SelectItem(8, "Java SE 8"));

        dockerFlags.put(false, new SelectItem(false, "No"));
        dockerFlags.put(true, new SelectItem(true, "Yes"));

        runtimes.put("", new SelectItem("", "-- Select runtime --", null, true));
        runtimes.put("none", new SelectItem("none", "None"));

        List<String> shuffledRuntimes = new ArrayList<>(RUNTIMES.keySet());
        Collections.shuffle(shuffledRuntimes); // Ensure random order every time.
        shuffledRuntimes.forEach(
                r -> runtimes.put(r, new SelectItem(r, RUNTIMES.get(r))));
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

    public boolean isProfileDisabled() {
        return profileDisabled;
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

    public boolean isJavaVersionDisabled() {
        return javaVersionDisabled;
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

    public boolean isDockerDisabled() {
        return dockerDisabled;
    }

    public Collection<SelectItem> getDockerFlags() {
        return dockerFlags.values();
    }

    public boolean isDocker() {
        return docker;
    }

    public void setDocker(boolean docker) {
        this.docker = docker;
    }

    public boolean isRuntimeDisabled() {
        return runtimeDisabled;
    }

    public Collection<SelectItem> getRuntimes() {
        return runtimes.values();
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public boolean isGenerateDisabled() {
        return generateDisabled;
    }

    public void onJakartaVersionChange() {
        LOGGER.log(Level.INFO, "Jakarta EE version selected: {0}", jakartaVersion);

        generateDisabled = true;
        profileDisabled = false;
        profile = "";
        javaVersionDisabled = true;
        javaVersion = 0;
        dockerDisabled = true;
        docker = false;
        runtimeDisabled = true;
        runtime = "";

        profiles.get("core").setDisabled(jakartaVersion < 10);
    }

    public void onProfileChange() {
        LOGGER.log(Level.INFO, "Jakarta EE Profile selected: {0}", profile);

        generateDisabled = true;
        javaVersionDisabled = false;
        javaVersion = 0;
        dockerDisabled = true;
        docker = false;
        runtimeDisabled = true;
        runtime = "";

        javaVersions.get(8).setDisabled(jakartaVersion >= 10);
        javaVersions.get(11).setDisabled(jakartaVersion >= 11);
    }

    public void onJavaVersionChange() {
        LOGGER.log(Level.INFO, "Java SE version selected: {0}", javaVersion);

        generateDisabled = true;
        dockerDisabled = false;
        docker = false;
        runtimeDisabled = false;
        runtime = "";

        updateRuntimeEnabledStates();
    }

    public void onDockerChange() {
        LOGGER.log(Level.INFO, "Docker option selected: {0}", docker);

        generateDisabled = true;
        runtime = "";

        updateRuntimeEnabledStates();
    }

    private void updateRuntimeEnabledStates() {
        runtimes.get("none").setDisabled(docker);

        if (jakartaVersion == 11) {
            // GlassFish does not support EE 11 yet
            runtimes.get("glassfish").setDisabled(true);
        } else if (profile.equals("core")) {
            // GlassFish does not support Core Profile
            runtimes.get("glassfish").setDisabled(true);
        } else if (jakartaVersion == 8 && javaVersion > 8) {
            // GlassFish 5 only supports Java SE 8
            runtimes.get("glassfish").setDisabled(true);
        } else if (docker && !(jakartaVersion == 10 && profile.equals("full") && javaVersion == 17)) {
            // GlassFish with Docker only supported with EE 10 + platform + Java 17
            runtimes.get("glassfish").setDisabled(true);
        } else {
            runtimes.get("glassfish").setDisabled(false);
        }

        if (jakartaVersion == 11) {
            runtimes.get("open-liberty").setDisabled(true);
        } else {
            runtimes.get("open-liberty").setDisabled(false);
        }

        if (jakartaVersion == 9 || jakartaVersion == 9.1) {
            // Payara does not offer a stable release for EE 9/9.1
            runtimes.get("payara").setDisabled(true);
        } else {
            runtimes.get("payara").setDisabled(false);
        }

        if (jakartaVersion > 9.1) {
            // TomEE does not support EE 10+
            runtimes.get("tomee").setDisabled(true);
        } else if (jakartaVersion == 9) {
            // TomEE is certified for 9.1, not 9
            runtimes.get("tomee").setDisabled(true);
        } else if (!profile.equals("web")) {
            // TomEE only supports Web Profile
            runtimes.get("tomee").setDisabled(true);
        } else if (jakartaVersion != 8 && javaVersion == 8) {
            // TomEE 9 does not support Java SE 8
            runtimes.get("tomee").setDisabled(true);
        } else {
            runtimes.get("tomee").setDisabled(false);
        }

        if (jakartaVersion == 9 || jakartaVersion == 9.1 || jakartaVersion == 11) {
            // No stable release for EE 9/9.1, doesn't support EE 11 yet
            runtimes.get("wildfly").setDisabled(true);
        } else {
            runtimes.get("wildfly").setDisabled(false);
        }
    }

    public void onRuntimeChange() {
        LOGGER.log(Level.INFO, "Runtime selected: {0}", runtime);

        generateDisabled = false;
    }

    public void generate() {
        try {
            LOGGER.log(Level.INFO,
                    "Generating project - Jakarta EE version: {0}, Jakarta EE Profile: {1}, Java SE version: {2}, Docker: {3}, runtime: {4}, groupId: {5}, artifactId: {6}",
                    new Object[]{jakartaVersion, profile, javaVersion, docker, runtime, groupId, artifactId});

            String cachedDirectory = cache.get(getCacheKey());

            if ((cachedDirectory == null)
                    || (!new File(cachedDirectory).exists())) {
                // This will result in a random directory like: starter-output-XYZ123
                File workingDirectory = Files
                        .createTempDirectory("starter-output-").toFile();
                LOGGER.log(Level.INFO, "Working directory: {0}",
                        new Object[]{workingDirectory.getAbsolutePath()});

                LOGGER.info("Executing Maven Archetype.");
                Properties properties = new Properties();
                properties.putAll(Map.ofEntries(
                    entry("jakartaVersion",
                        ((jakartaVersion % 1.0 != 0)
                            ? String.format("%s", jakartaVersion)
                            : String.format("%.0f", jakartaVersion)
                        )
                    ),
                    entry("profile", profile),
                    entry("javaVersion", javaVersion),
                    entry("docker", (docker ? "yes" : "no")),
                    entry("runtime", runtime),
                    entry("groupId", groupId),
                    entry("artifactId", artifactId),
                    entry("package", groupId)
                ));

                MavenUtility.invokeMavenArchetype("org.eclipse.starter",
                    "jakarta-starter", ARCHETYPE_VERSION,
                        properties, workingDirectory);

                LOGGER.info("Creating zip file.");
                ZipUtility.zipDirectory(new File(workingDirectory, artifactId),
                        workingDirectory);

                LOGGER.info("Downloading zip file.");
                downloadZip(new File(workingDirectory, artifactId + ".zip"));

                // Caching makes only sense if defaults weren't changed since
                // otherwise it's unlikely to hit the cache.
                if (groupId.equals(DEFAULT_GROUPID)
                        && artifactId.equals(DEFAULT_ARTIFACTID)) {
                    LOGGER.info("Caching output.");
                    cache.put(getCacheKey(),
                            workingDirectory.getAbsolutePath());
                }

                workingDirectory.deleteOnExit();
            } else {
                LOGGER.log(Level.INFO,
                        "Downloading zip file from cached directory: {0}",
                        new Object[]{cachedDirectory});
                downloadZip(new File(cachedDirectory, artifactId + ".zip"));
            }

            facesContext.responseComplete();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate zip.", e);
        }
    }

    private String getCacheKey() {
        // Need the groupId and artifactId in the key to avoid returning the default cached zip
        return jakartaVersion + ":" + profile + ":" + javaVersion + ":" + docker
                + ":" + runtime + ":" + groupId + ":" + artifactId;
    }

    private void downloadZip(File zip) {
        try {
            // Some component library or filter might have set some headers in
            // the buffer beforehand. We want to get rid of them, else they may
            // collide.
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
