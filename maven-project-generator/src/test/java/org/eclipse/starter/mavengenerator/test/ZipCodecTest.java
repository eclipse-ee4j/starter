package org.eclipse.starter.mavengenerator.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;
import org.eclipse.starter.mavengenerator.ZipCodec;
import org.eclipse.starter.mavengenerator.test.commons.UnitTestCommons;
import org.junit.jupiter.api.TestInfo;

public class ZipCodecTest implements UnitTestCommons {

    @Test
    @DisplayName("Can compress test directory")
    void canCompressTestDirectory(TestInfo testInfo) throws IOException, Exception {
        String compressDirPath = System.getProperty("test.project.dir");
        assertThat(compressDirPath).as("Path to test directory").isNotNull();
        
        File testDir = new File(compressDirPath);
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutStream = new ZipOutputStream(bytesOutputStream)) {
            new ZipCodec()
                    .addDirToZipArchive(testDir, zipOutStream);
        }
        
        Files.write(getTestDir(testInfo).toPath().resolve(getTestMethodName(testInfo) + ".zip"), 
                bytesOutputStream.toByteArray(), StandardOpenOption.CREATE);
    }
}
