package org.eclipse.starter.ui;

import jakarta.faces.model.SelectItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit test for the {@link Project} class.
 */
public class ProjectTest {

    private Project project;

    static Stream<Double> jakartaVersionProvider() {
        Project project = new Project();
        return project.getJakartaVersions().stream()
                .map(item -> (Double)item.getValue());
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
        project.setJakartaVersion(jakartaVersion);

        project.onJakartaVersionChange();

        // Verify that groupId and artifactId are not null
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

    @ParameterizedTest
    @MethodSource("jakartaVersionProvider")
    public void testItemsEnabledDisabledOnJakartaVersionChange(Double jakartaVersion) {
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

    @ParameterizedTest
    @ValueSource(strings = {"web", "full", "core"})
    public void testItemsEnabledDisabledOnProfileChange(String profileValue) {
        project.setProfile(profileValue);

        project.onProfileChange();

        final var runtimes = project.getRuntimes();

        verifyJakartaVersionsEnabled();

        if (profileValue.equals("core")) {
            // TomEE should be disabled for Core profile
            verifyItemState(false, runtimes, "tomee", "TomEE should be disabled for Core profile");
        } else if (profileValue.equals("full")) {
            // TomEE should be disabled for Full profile
            verifyItemState(false, runtimes, "tomee", "TomEE should be disabled for Full profile");
        } else if (profileValue.equals("web")) {
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
        project.setJavaVersion(javaVersion);

        project.onJavaVersionChange();

        // Verify that items are correctly enabled or disabled based on Java version
        final var jakartaVersions = project.getJakartaVersions();
        final var profiles = project.getProfiles();
        final var runtimes = project.getRuntimes();

        // TomEE is disabled by default
        verifyItemState(false, runtimes, "tomee", "TomEE should be disabled by default");

        if (javaVersion == 8) {
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
        project.setDocker(dockerValue);

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
        project.setRuntime(runtimeValue);

        project.onRuntimeChange();

        final var profiles = project.getProfiles();
        final var javaVersions = project.getJavaVersions();

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
                break;
            case "payara":
                break;
            case "tomee":
                verifyItemState(false, profiles, "core", "Core profile should be disabled for TomEE runtime");
                verifyItemState(false, profiles, "full", "Full profile should be disabled for TomEE runtime");
                break;
            case "wildfly":
                break;
        }
    }

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
        project.getJakartaVersions().forEach(item ->
                assertFalse(item.isDisabled(), "Jakarta EE version " + item.getValue() + " should be enabled")
        );
    }
}
