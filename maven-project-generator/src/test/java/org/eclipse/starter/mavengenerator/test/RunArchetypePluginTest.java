package org.eclipse.starter.mavengenerator.test;

import org.eclipse.starter.mavengenerator.test.commons.UnitTestCommons;
import java.io.File;
import org.eclipse.starter.mavengenerator.ArchetypeGenerateParameters;
import org.eclipse.starter.mavengenerator.CliMavenContext;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class RunArchetypePluginTest implements UnitTestCommons {

    @Test
    void canRunCliArchetypePluginFromParams(TestInfo testInfo) {
        String newArtifactName = getTestMethodName(testInfo);
        File testDir = getTestDir(testInfo);

        boolean result = new ArchetypeGenerateParameters()
                .archetypeGroupId("org.eclipse.starter")
                .archetypeArtifactId("jakartaee10-minimal")
                .archetypeVersion("1.1.0")
                .groupId("org.eclipse.starter.test")
                .artifactId(newArtifactName)
                .version("0.1-SNAPSHOT")
                .interactiveMode(false)
                .addGoal("org.apache.maven.plugins:maven-archetype-plugin:3.2.1:generate")
                .updateMavenContext(new CliMavenContext()
                        .workingDirectory(testDir)
                        .addOptions("-X")
                )
                .run();

        assertThat(result).as("Result of Maven execution").isTrue();
        assertThat(new File(testDir, newArtifactName + File.separator + "pom.xml")).as("Generated pom.xml file").exists();
    }
}
