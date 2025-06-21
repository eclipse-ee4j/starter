package org.eclipse.starter.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import jakarta.faces.model.SelectItem;

import static java.util.Collections.shuffle;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit test for the {@link Project} class.
 */
public class ProjectTest {

    private Project project;

    static Stream<Double> jakartaVersionProvider() {
        // Get all Jakarta versions from the Project class
        Project project = new Project();
        return project.getJakartaVersions().stream()
                .map(item -> Double.parseDouble(item.getValue().toString()));
    }

    @BeforeEach
    public void setUp() {
        project = new Project();
    }

    @Test
    public void testInitialState() {
        assertEquals(Project.DEFAULT_JAKARTA_VERSION, project.getJakartaVersion(), "Default Jakarta version should match Project.DEFAULT_JAKARTA_VERSION");
        assertEquals(Project.DEFAULT_PROFILE, project.getProfile(), "Default profile should match Project.DEFAULT_PROFILE");
        assertEquals(Project.DEFAULT_JAVA_VERSION, project.getJavaVersion(), "Default Java version should match Project.DEFAULT_JAVA_VERSION");
        assertEquals(Project.DEFAULT_RUNTIME, project.getRuntime(), "Default runtime should match Project.DEFAULT_RUNTIME");
        assertFalse(project.isDocker(), "Default Docker support should be false");
        verifyJakartaVersionsEnabled();
    }

    @ParameterizedTest
    @MethodSource("jakartaVersionProvider")
    public void testFormValuesOnJakartaVersionChange(Double jakartaVersion) {
        // Set the specific Jakarta version for this test iteration
        project.setJakartaVersion(jakartaVersion);

        project.onJakartaVersionChange();

        // Verify that jakartaVersion, groupId, and artifactId are not null
        assertNotNull(project.getJakartaVersion(), "jakartaVersion should not be null");
        assertNotNull(project.getGroupId(), "groupId should not be null");
        assertNotNull(project.getArtifactId(), "artifactId should not be null");

        // Verify that all other writeable properties set to their default values
        assertEquals(Project.DEFAULT_PROFILE, project.getProfile(), "profile should be reset to default");
        assertEquals(Project.DEFAULT_JAVA_VERSION, project.getJavaVersion(), "javaVersion should be reset to default");
        assertFalse(project.isDocker(), "docker should be false");
        assertEquals(Project.DEFAULT_RUNTIME, project.getRuntime(), "runtime should be reset to default");

        // Verify this specific jakartaVersion is set
        assertEquals(jakartaVersion, project.getJakartaVersion(), "jakartaVersion should still be set to the test parameter");
    }

    @Test
    public void testJakartaVersionsEnabledOnJakartaVersionChange() {
        // Set the specific Jakarta version for this test iteration
        project.setJakartaVersion(Project.DEFAULT_JAKARTA_VERSION);

        // Select a random number of items and set disabled to true
        final var jakartaVersions = new ArrayList<>(project.getJakartaVersions());
        shuffle(jakartaVersions);
        jakartaVersions.stream().limit((int) (Math.random() * jakartaVersions.size()))
                .forEach(item -> item.setDisabled(true));

        project.onJakartaVersionChange();

        verifyJakartaVersionsEnabled();
    }


    @ParameterizedTest
    @MethodSource("jakartaVersionProvider")
    public void testItemsEnabledDisabledOnJakartaVersionChange(Double jakartaVersion) {
        // Set the specific Jakarta version for this test iteration
        project.setJakartaVersion(jakartaVersion);

        project.onJakartaVersionChange();

        final var profiles = project.getProfiles();
        final var javaVersions = project.getJavaVersions();
        final var runtimes = project.getRuntimes();

        if (jakartaVersion == 11) {
            // For Jakarta EE 11, the Full profile is actually enabled in the implementation
            verifyItemState(false, profiles, "web", "Web profile should be disabled for Jakarta EE 11");
            verifyItemState(true, profiles, "core", "Core profile should be enabled for Jakarta EE 11");

            verifyItemState(false, javaVersions, "11", "Java 11 should be disabled for Jakarta EE 11");
            verifyItemState(true, javaVersions, "17", "Java 17 should be enabled for Jakarta EE 11");
            verifyItemState(true, javaVersions, "21", "Java 21 should be enabled for Jakarta EE 11");
            verifyItemState(false, javaVersions, "8", "Java 8 should be disabled for Jakarta EE 11");

            verifyItemState(false, runtimes, "glassfish", "GlassFish should be disabled for Jakarta EE 11");
            verifyItemState(false, runtimes, "payara", "Payara should be disabled for Jakarta EE 11");
            verifyItemState(false, runtimes, "tomee", "TomEE should be disabled for Jakarta EE 11");
            verifyItemState(false, runtimes, "wildfly", "WildFly should be disabled for Jakarta EE 11");
        } else if (jakartaVersion == 10) {
            // For Jakarta EE 10, the Full profile is actually enabled in the implementation
            verifyItemState(true, profiles, "web", "Web profile should be enabled for Jakarta EE 10");
            verifyItemState(true, profiles, "core", "Core profile should be enabled for Jakarta EE 10");

            verifyItemState(true, javaVersions, "11", "Java 11 should be enabled for Jakarta EE 10");
            verifyItemState(true, javaVersions, "17", "Java 17 should be enabled for Jakarta EE 10");
            verifyItemState(true, javaVersions, "21", "Java 21 should be enabled for Jakarta EE 10");
            verifyItemState(false, javaVersions, "8", "Java 8 should be disabled for Jakarta EE 10");

            verifyItemState(true, runtimes, "glassfish", "GlassFish should be enabled for Jakarta EE 10");
            verifyItemState(true, runtimes, "payara", "Payara should be enabled for Jakarta EE 10");
            verifyItemState(false, runtimes, "tomee", "TomEE should be disabled for Jakarta EE 10");
            verifyItemState(true, runtimes, "wildfly", "WildFly should be enabled for Jakarta EE 10");
        } else if (jakartaVersion == 9.1 || jakartaVersion == 9) {
            // For Jakarta EE 9/9.1, the Full profile is actually enabled in the implementation
            verifyItemState(true, profiles, "web", "Web profile should be enabled for Jakarta EE 9/9.1");
            verifyItemState(false, profiles, "core", "Core profile should be disabled for Jakarta EE 9/9.1");

            verifyItemState(true, javaVersions, "11", "Java 11 should be enabled for Jakarta EE 9/9.1");
            verifyItemState(true, javaVersions, "17", "Java 17 should be enabled for Jakarta EE 9/9.1");
            verifyItemState(true, javaVersions, "21", "Java 21 should be enabled for Jakarta EE 9/9.1");
            verifyItemState(true, javaVersions, "8", "Java 8 should be enabled for Jakarta EE 9/9.1");

            verifyItemState(true, runtimes, "glassfish", "GlassFish should be enabled for Jakarta EE 9/9.1");
            verifyItemState(false, runtimes, "payara", "Payara should be disabled for Jakarta EE 9/9.1");
            verifyItemState(false, runtimes, "tomee", "TomEE should be disabled for Jakarta EE 9/9.1");
            verifyItemState(false, runtimes, "wildfly", "WildFly should be disabled for Jakarta EE 9/9.1");
        } else if (jakartaVersion == 8) {
            // For Jakarta EE 8, the Full profile is actually enabled in the implementation
            verifyItemState(true, profiles, "web", "Web profile should be enabled for Jakarta EE 8");
            verifyItemState(false, profiles, "core", "Core profile should be disabled for Jakarta EE 8");

            verifyItemState(true, javaVersions, "8", "Java 8 should be enabled for Jakarta EE 8");
            verifyItemState(true, javaVersions, "11", "Java 11 should be enabled for Jakarta EE 8");
            verifyItemState(true, javaVersions, "17", "Java 17 should be enabled for Jakarta EE 8");
            verifyItemState(true, javaVersions, "21", "Java 21 should be enabled for Jakarta EE 8");

            verifyItemState(false, runtimes, "glassfish", "GlassFish should be disabled for Jakarta EE 8");
            verifyItemState(true, runtimes, "payara", "Payara should be enabled for Jakarta EE 8");
            verifyItemState(false, runtimes, "tomee", "TomEE should be disabled for Jakarta EE 8");
            verifyItemState(true, runtimes, "wildfly", "WildFly should be enabled for Jakarta EE 8");
        }
    }

    // Helper method to find a SelectItem by its value
    private SelectItem getSelectItemByValue(java.util.Collection<SelectItem> items, String value) {
        return items.stream()
                .filter(item -> item.getValue().toString().equals(value))
                .findFirst()
                .orElseThrow(() -> new AssertionError("SelectItem with value '" + value + "' not found"));
    }

    private void verifyItemState(boolean shouldBeEnabled, java.util.Collection<SelectItem> items, String value, String message) {
        assertEquals(shouldBeEnabled, !getSelectItemByValue(items, value).isDisabled(), message);
    }

    private void verifyJakartaVersionsEnabled() {
        // In the initial state, Jakarta EE 11 and 10 should be enabled, while 9.1, 9, and 8 are disabled
        verifyItemState(true, project.getJakartaVersions(), "11", "Jakarta EE 11 should be enabled");
        verifyItemState(true, project.getJakartaVersions(), "10", "Jakarta EE 10 should be enabled");
    }

    @ParameterizedTest
    @ValueSource(strings = {"web", "full", "core"})
    public void testItemsEnabledDisabledOnProfileChange(String profileValue) {
        // Set the specific profile for this test iteration
        project.setProfile(profileValue);

        // Execute the method under test
        project.onProfileChange();

        // Verify that items are correctly enabled or disabled based on profile
        final var jakartaVersions = project.getJakartaVersions();
        final var runtimes = project.getRuntimes();

        if (profileValue.equals("core")) {
            // Core profile disables Jakarta EE 8, 9, and 9.1
            verifyItemState(false, jakartaVersions, "8", "Jakarta EE 8 should be disabled for Core profile");
            verifyItemState(false, jakartaVersions, "9", "Jakarta EE 9 should be disabled for Core profile");
            verifyItemState(false, jakartaVersions, "9.1", "Jakarta EE 9.1 should be disabled for Core profile");

            // TomEE should be disabled for Core profile
            verifyItemState(false, runtimes, "tomee", "TomEE should be disabled for Core profile");

            // Jakarta EE 11 might be enabled if conditions are met
            if ((project.getRuntime().equals("none") || project.getRuntime().equals("open-liberty"))
                    && (project.getJavaVersion() > 11)) {
                verifyItemState(true, jakartaVersions, "11", "Jakarta EE 11 should be enabled for Core profile with compatible runtime and Java version");
            } else {
                verifyItemState(false, jakartaVersions, "11", "Jakarta EE 11 should be disabled by default");
            }
        } else if (profileValue.equals("full")) {
            // Jakarta EE 11 is disabled by default for non-core profiles
            verifyItemState(false, jakartaVersions, "11", "Jakarta EE 11 should be disabled by default for Full profile");

            // TomEE should be disabled for Full profile
            verifyItemState(false, runtimes, "tomee", "TomEE should be disabled for Full profile");
        } else if (profileValue.equals("web")) {
            // Jakarta EE 11 is disabled by default for non-core profiles
            verifyItemState(false, jakartaVersions, "11", "Jakarta EE 11 should be disabled by default for Web profile");

            // For Web profile, TomEE might be enabled if conditions are met
            if ((project.getJakartaVersion() == 8) ||
                    ((project.getJakartaVersion() == 9.1) && (project.getJavaVersion() != 8))) {
                verifyItemState(true, runtimes, "tomee", "TomEE should be enabled for Web profile with compatible Jakarta and Java versions");
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 11, 17, 21})
    public void testItemsEnabledDisabledOnJavaVersionChange(int javaVersion) {
        // Set the specific Java version for this test iteration
        project.setJavaVersion(javaVersion);

        // Execute the method under test
        project.onJavaVersionChange();

        // Verify that items are correctly enabled or disabled based on Java version
        final var jakartaVersions = project.getJakartaVersions();
        final var profiles = project.getProfiles();
        final var runtimes = project.getRuntimes();

        // Jakarta EE 11 is disabled by default
        verifyItemState(false, jakartaVersions, "11", "Jakarta EE 11 should be disabled by default");

        // TomEE is disabled by default
        verifyItemState(false, runtimes, "tomee", "TomEE should be disabled by default");

        if (javaVersion == 8) {
            // Java 8 disables Jakarta EE 10
            verifyItemState(false, jakartaVersions, "10", "Jakarta EE 10 should be disabled for Java 8");

            // Core profile should be disabled for Java 8
            verifyItemState(false, profiles, "core", "Core profile should be disabled for Java 8");

            // TomEE might be enabled for Web profile and Jakarta EE 8
            if (project.getProfile().equals("web") && (project.getJakartaVersion() == 8)) {
                verifyItemState(true, runtimes, "tomee", "TomEE should be enabled for Web profile and Jakarta EE 8 with Java 8");
            }

            // Payara and WildFly might be disabled if not Jakarta EE 8
            if (project.getJakartaVersion() != 8) {
                verifyItemState(false, runtimes, "payara", "Payara should be disabled for Java 8 with Jakarta EE version other than 8");
                verifyItemState(false, runtimes, "wildfly", "WildFly should be disabled for Java 8 with Jakarta EE version other than 8");
            } else {
                // For Jakarta EE 8, Payara and WildFly should be enabled
                verifyItemState(true, runtimes, "payara", "Payara should be enabled for Java 8 with Jakarta EE 8");
                verifyItemState(true, runtimes, "wildfly", "WildFly should be enabled for Java 8 with Jakarta EE 8");
            }
        } else {
            // For Java versions > 8

            // Jakarta EE 10 should be enabled if not TomEE
            if (!project.getRuntime().equals("tomee")) {
                verifyItemState(true, jakartaVersions, "10", "Jakarta EE 10 should be enabled for Java > 8 with compatible runtime");
            }

            // Jakarta EE 11 might be enabled for Core profile and Java > 11
            if ((javaVersion > 11) && project.getProfile().equals("core")
                    && (project.getRuntime().equals("open-liberty") || project.getRuntime().equals("none"))) {
                verifyItemState(true, jakartaVersions, "11", "Jakarta EE 11 should be enabled for Core profile with Java > 11 and compatible runtime");
            }

            // Core profile might be enabled for Jakarta EE > 9.1
            if ((project.getJakartaVersion() > 9.1) && !project.getRuntime().equals("tomee")
                    && !project.getRuntime().equals("glassfish")) {
                verifyItemState(true, profiles, "core", "Core profile should be enabled for Jakarta EE > 9.1 with compatible runtime");
            }

            // TomEE might be enabled for Web profile and compatible Jakarta versions
            if (project.getProfile().equals("web")
                    && ((project.getJakartaVersion() == 8) || (project.getJakartaVersion() == 9.1))) {
                verifyItemState(true, runtimes, "tomee", "TomEE should be enabled for Web profile with Jakarta EE 8 or 9.1 and Java > 8");
            }

            // Payara and WildFly should be enabled for Jakarta EE versions other than 9, 9.1, and 11
            if ((project.getJakartaVersion() != 9) && (project.getJakartaVersion() != 9.1)
                    && (project.getJakartaVersion() != 11)) {
                verifyItemState(true, runtimes, "payara", "Payara should be enabled for Jakarta EE versions other than 9, 9.1, and 11");
                verifyItemState(true, runtimes, "wildfly", "WildFly should be enabled for Jakarta EE versions other than 9, 9.1, and 11");
            }
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    public void testItemsEnabledDisabledOnDockerChange(boolean dockerValue) {
        // Set the specific Docker value for this test iteration
        project.setDocker(dockerValue);

        // Execute the method under test
        project.onDockerChange();

        // Verify that items are correctly enabled or disabled based on Docker support
        final var runtimes = project.getRuntimes();

        if (dockerValue) {
            // When Docker is enabled, "none" runtime should be disabled
            verifyItemState(false, runtimes, "none", "None runtime should be disabled when Docker is enabled");

            // GlassFish might be disabled based on Jakarta version, profile, and Java version
            if (!(project.getJakartaVersion() == 10 && project.getProfile().equals("full") && project.getJavaVersion() == 17)) {
                verifyItemState(false, runtimes, "glassfish", "GlassFish should be disabled when Docker is enabled unless specific conditions are met");
            }
        } else {
            // When Docker is disabled, "none" runtime should be enabled
            verifyItemState(true, runtimes, "none", "None runtime should be enabled when Docker is disabled");

            // GlassFish might be enabled or disabled based on other conditions
            if ((project.getJakartaVersion() == 8 && project.getJavaVersion() > 8) ||
                    project.getJakartaVersion() == 11 ||
                    project.getProfile().equals("core")) {
                verifyItemState(false, runtimes, "glassfish", "GlassFish should be disabled based on Jakarta version, profile, or Java version");
            } else {
                verifyItemState(true, runtimes, "glassfish", "GlassFish should be enabled when conditions allow");
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"none", "glassfish", "open-liberty", "payara", "tomee", "wildfly"})
    public void testItemsEnabledDisabledOnRuntimeChange(String runtimeValue) {
        // Set the specific runtime for this test iteration
        project.setRuntime(runtimeValue);

        // Execute the method under test
        project.onRuntimeChange();

        // Verify that items are correctly enabled or disabled based on runtime
        final var jakartaVersions = project.getJakartaVersions();
        final var profiles = project.getProfiles();
        final var javaVersions = project.getJavaVersions();

        // Common assertions for all runtimes
        // Check if Jakarta EE 9 and 9.1 are enabled/disabled based on runtime
        if (runtimeValue.equals("payara") || runtimeValue.equals("wildfly") || runtimeValue.equals("tomee")) {
            // These runtimes disable Jakarta EE 9 and 9.1
            if (runtimeValue.equals("payara") || runtimeValue.equals("wildfly")) {
                verifyItemState(false, jakartaVersions, "9", "Jakarta EE 9 should be disabled for " + runtimeValue);
                verifyItemState(false, jakartaVersions, "9.1", "Jakarta EE 9.1 should be disabled for " + runtimeValue);
            } else if (runtimeValue.equals("tomee")) {
                verifyItemState(false, jakartaVersions, "9", "Jakarta EE 9 should be disabled for TomEE");
            }
        } else if (!project.getProfile().equals("core")) {
            // For other runtimes with non-core profiles, Jakarta EE 9 and 9.1 should be enabled
            verifyItemState(true, jakartaVersions, "9", "Jakarta EE 9 should be enabled for non-core profiles with " + runtimeValue);
            verifyItemState(true, jakartaVersions, "9.1", "Jakarta EE 9.1 should be enabled for non-core profiles with " + runtimeValue);
        }

        // Check Jakarta EE 10 enabled state
        if (runtimeValue.equals("tomee") || project.getJavaVersion() == 8) {
            verifyItemState(false, jakartaVersions, "10", "Jakarta EE 10 should be disabled for TomEE or Java 8");
        } else {
            verifyItemState(true, jakartaVersions, "10", "Jakarta EE 10 should be enabled for Java versions > 8 with compatible runtime");
        }

        // Jakarta EE 11 is disabled by default, but might be enabled in specific cases
        if ((runtimeValue.equals("none") || runtimeValue.equals("open-liberty")) &&
                project.getProfile().equals("core") && project.getJavaVersion() > 11) {
            verifyItemState(true, jakartaVersions, "11", "Jakarta EE 11 should be enabled for Core profile with Java > 11 and " + runtimeValue);
        } else {
            verifyItemState(false, jakartaVersions, "11", "Jakarta EE 11 should be disabled by default");
        }

        // Check Core profile enabled state
        if (runtimeValue.equals("glassfish") || runtimeValue.equals("tomee")) {
            verifyItemState(false, profiles, "core", "Core profile should be disabled for " + runtimeValue);
        } else if (project.getJakartaVersion() > 9.1) {
            verifyItemState(true, profiles, "core", "Core profile should be enabled for Jakarta EE versions > 9.1 with " + runtimeValue);
        }

        // Check Full profile enabled state
        if (runtimeValue.equals("tomee")) {
            verifyItemState(false, profiles, "full", "Full profile should be disabled for TomEE");
        } else if (project.getJakartaVersion() != 11) {
            verifyItemState(true, profiles, "full", "Full profile should be enabled for Jakarta EE versions != 11 with " + runtimeValue);
        } else {
            verifyItemState(false, profiles, "full", "Full profile should be disabled for Jakarta EE 11");
        }

        if (project.getJakartaVersion() < 10) {
            verifyItemState(true, javaVersions, "8", "Java 8 should be enabled for Jakarta EE versions < 10");
        }

        if (project.getJakartaVersion() < 11) {
            verifyItemState(true, javaVersions, "11", "Java 11 should be enabled for Jakarta EE versions < 11");
        }

        // Java 17 and 21 should always be enabled
        verifyItemState(true, javaVersions, "17", "Java 17 should always be enabled");
        verifyItemState(true, javaVersions, "21", "Java 21 should always be enabled");

        // Runtime-specific assertions
        switch (runtimeValue) {
            case "none":
                if (project.getProfile().equals("core") && (project.getJavaVersion() > 11)) {
                    verifyItemState(true, jakartaVersions, "11", "Jakarta EE 11 should be enabled for Core profile with Java > 11 and None runtime");
                }
                break;
            case "glassfish":
                verifyItemState(false, profiles, "core", "Core profile should be disabled for GlassFish runtime");

                if (project.getJakartaVersion() == 8) {
                    verifyItemState(false, javaVersions, "11", "Java 11 should be disabled for GlassFish with Jakarta EE 8");
                    verifyItemState(false, javaVersions, "17", "Java 17 should be disabled for GlassFish with Jakarta EE 8");
                    verifyItemState(false, javaVersions, "21", "Java 21 should be disabled for GlassFish with Jakarta EE 8");
                }
                break;
            case "open-liberty":
                if (project.getProfile().equals("core") && (project.getJavaVersion() > 11)) {
                    verifyItemState(true, jakartaVersions, "11", "Jakarta EE 11 should be enabled for Core profile with Java > 11 and Open Liberty runtime");
                }
                break;
            case "payara":
                verifyItemState(false, jakartaVersions, "9", "Jakarta EE 9 should be disabled for Payara runtime");
                verifyItemState(false, jakartaVersions, "9.1", "Jakarta EE 9.1 should be disabled for Payara runtime");
                break;
            case "tomee":
                verifyItemState(false, jakartaVersions, "9", "Jakarta EE 9 should be disabled for TomEE runtime");
                verifyItemState(false, jakartaVersions, "10", "Jakarta EE 10 should be disabled for TomEE runtime");
                verifyItemState(false, profiles, "core", "Core profile should be disabled for TomEE runtime");
                verifyItemState(false, profiles, "full", "Full profile should be disabled for TomEE runtime");

                if (project.getJakartaVersion() != 8) {
                    verifyItemState(false, javaVersions, "8", "Java 8 should be disabled for TomEE with Jakarta EE version other than 8");
                }
                break;
            case "wildfly":
                verifyItemState(false, jakartaVersions, "9", "Jakarta EE 9 should be disabled for WildFly runtime");
                verifyItemState(false, jakartaVersions, "9.1", "Jakarta EE 9.1 should be disabled for WildFly runtime");
                break;
        }
    }
}
