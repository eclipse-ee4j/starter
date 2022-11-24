package org.eclipse.starter.mavengenerator.test;

import org.eclipse.starter.mavengenerator.test.commons.UnitTestCommons;
import org.eclipse.starter.mavengenerator.CliMavenContext;
import org.eclipse.starter.mavengenerator.MavenParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;

public class RunMavenTest implements UnitTestCommons {

    @Test
    @DisplayName("Can run Cli Maven")
    void canRunCliMaven(TestInfo testInfo) {
        boolean result = new MavenParameters()
                .addGoal("-version")
                .updateMavenContext(new CliMavenContext()
                        .workingDirectory(this.getTestDir(testInfo)))
                .run();
        assertThat(result).as("Result of Maven execution").isTrue();
    }

}
