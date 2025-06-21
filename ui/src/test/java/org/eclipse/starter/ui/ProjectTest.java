package org.eclipse.starter.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collection;
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

        // Execute the method under test
        project.onJakartaVersionChange();

        // Verify that items are correctly enabled or disabled based on Jakarta version
        final var profiles = project.getProfiles();
        final var javaVersions = project.getJavaVersions();
        final var runtimes = project.getRuntimes();

        if (jakartaVersion == 11) {
            verifyItemEnabled(false, profiles, "full", "Full profile should be disabled for Jakarta EE 11");
            verifyItemEnabled(false, profiles, "web", "Web profile should be disabled for Jakarta EE 11");
            verifyItemEnabled(true, profiles, "core", "Core profile should be enabled for Jakarta EE 11");

            verifyItemEnabled(false, javaVersions, "11", "Java 11 should be disabled for Jakarta EE 11");
            verifyItemEnabled(true, javaVersions, "17", "Java 17 should be enabled for Jakarta EE 11");
            verifyItemEnabled(true, javaVersions, "21", "Java 21 should be enabled for Jakarta EE 11");
            verifyItemEnabled(false, javaVersions, "8", "Java 8 should be disabled for Jakarta EE 11");

            verifyItemEnabled(false, runtimes, "glassfish", "GlassFish should be disabled for Jakarta EE 11");
            verifyItemEnabled(false, runtimes, "payara", "Payara should be disabled for Jakarta EE 11");
            verifyItemEnabled(false, runtimes, "tomee", "TomEE should be disabled for Jakarta EE 11");
            verifyItemEnabled(false, runtimes, "wildfly", "WildFly should be disabled for Jakarta EE 11");
        } else if (jakartaVersion == 10) {
            verifyItemEnabled(false, profiles, "full", "Full profile should be disabled for Jakarta EE 10");
            verifyItemEnabled(true, profiles, "web", "Web profile should be enabled for Jakarta EE 10");
            verifyItemEnabled(true, profiles, "core", "Core profile should be enabled for Jakarta EE 10");

            verifyItemEnabled(true, javaVersions, "11", "Java 11 should be enabled for Jakarta EE 10");
            verifyItemEnabled(true, javaVersions, "17", "Java 17 should be enabled for Jakarta EE 10");
            verifyItemEnabled(true, javaVersions, "21", "Java 21 should be enabled for Jakarta EE 10");
            verifyItemEnabled(false, javaVersions, "8", "Java 8 should be disabled for Jakarta EE 10");

            verifyItemEnabled(true, runtimes, "glassfish", "GlassFish should be enabled for Jakarta EE 10");
            verifyItemEnabled(true, runtimes, "payara", "Payara should be enabled for Jakarta EE 10");
            verifyItemEnabled(false, runtimes, "tomee", "TomEE should be disabled for Jakarta EE 10");
            verifyItemEnabled(true, runtimes, "wildfly", "WildFly should be enabled for Jakarta EE 10");
        } else if (jakartaVersion == 9.1 || jakartaVersion == 9) {
            verifyItemEnabled(false, profiles, "full", "Full profile should be disabled for Jakarta EE 9/9.1");
            verifyItemEnabled(true, profiles, "web", "Web profile should be enabled for Jakarta EE 9/9.1");
            verifyItemEnabled(false, profiles, "core", "Core profile should be disabled for Jakarta EE 9/9.1");

            verifyItemEnabled(true, javaVersions, "11", "Java 11 should be enabled for Jakarta EE 9/9.1");
            verifyItemEnabled(true, javaVersions, "17", "Java 17 should be enabled for Jakarta EE 9/9.1");
            verifyItemEnabled(true, javaVersions, "21", "Java 21 should be enabled for Jakarta EE 9/9.1");
            verifyItemEnabled(true, javaVersions, "8", "Java 8 should be enabled for Jakarta EE 9/9.1");

            verifyItemEnabled(true, runtimes, "glassfish", "GlassFish should be enabled for Jakarta EE 9/9.1");
            verifyItemEnabled(false, runtimes, "payara", "Payara should be disabled for Jakarta EE 9/9.1");
            verifyItemEnabled(false, runtimes, "tomee", "TomEE should be disabled for Jakarta EE 9/9.1");
            verifyItemEnabled(false, runtimes, "wildfly", "WildFly should be disabled for Jakarta EE 9/9.1");
        } else if (jakartaVersion == 8) {
            verifyItemEnabled(false, profiles, "full", "Full profile should be disabled for Jakarta EE 8");
            verifyItemEnabled(true, profiles, "web", "Web profile should be enabled for Jakarta EE 8");
            verifyItemEnabled(false, profiles, "core", "Core profile should be disabled for Jakarta EE 8");

            verifyItemEnabled(true, javaVersions, "8", "Java 8 should be enabled for Jakarta EE 8");
            verifyItemEnabled(true, javaVersions, "11", "Java 11 should be enabled for Jakarta EE 8");
            verifyItemEnabled(true, javaVersions, "17", "Java 17 should be enabled for Jakarta EE 8");
            verifyItemEnabled(true, javaVersions, "21", "Java 21 should be enabled for Jakarta EE 8");

            verifyItemEnabled(false, runtimes, "glassfish", "GlassFish should be disabled for Jakarta EE 8");
            verifyItemEnabled(true, runtimes, "payara", "Payara should be enabled for Jakarta EE 8");
            verifyItemEnabled(false, runtimes, "tomee", "TomEE should be disabled for Jakarta EE 8");
            verifyItemEnabled(true, runtimes, "wildfly", "WildFly should be enabled for Jakarta EE 8");
        }
    }

    // Helper method to find a SelectItem by its value
    private SelectItem getSelectItemByValue(java.util.Collection<SelectItem> items, String value) {
        return items.stream()
                .filter(item -> item.getValue().toString().equals(value))
                .findFirst()
                .orElseThrow(() -> new AssertionError("SelectItem with value '" + value + "' not found"));
    }

    private void verifyItemEnabled(boolean enabled, java.util.Collection<SelectItem> items, String value, String message) {
        assertTrue(enabled != getSelectItemByValue(items, value).isDisabled(), message);
    }

    private void verifyJakartaVersionsEnabled() {
        assertTrue(project.getJakartaVersions().stream()
                .noneMatch(item -> item.isDisabled()), "all Jakarta EE versions should be enabled");
    }
}
