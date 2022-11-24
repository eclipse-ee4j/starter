package org.eclipse.starter.mavengenerator.test.commons;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;

public interface UnitTestCommons {

    @BeforeAll
    static void clean() {
        try {
            Files.walk(Paths.get(getRootTestTempDir()))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (Exception e) {
            System.out.println("WARNING: " + e.getMessage());
        }
    }

    static String getRootTestTempDir() {
        return System.getProperty("test.temp.dir", System.getProperty("java.io.tmpdir", "noDefaultTempDir"));
    }
    
    default Path getTestDir(TestInfo testInfo) {
        Path testDir = Paths.get(getRootTestTempDir(), getTestMethodName(testInfo));
        try {
            Files.createDirectories(testDir);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return testDir;
    }
    
    default String getTestMethodName(TestInfo testInfo) {
        return testInfo.getTestMethod().map(Method::getName).orElse("anonymousTest");
    }
}
