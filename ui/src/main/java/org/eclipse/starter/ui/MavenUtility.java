package org.eclipse.starter.ui;

import static java.util.Map.entry;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.maven.cli.MavenCli;

public class MavenUtility {

  private static final Logger LOGGER = Logger.getLogger(
      MethodHandles.lookup().lookupClass().getName());

  public static void invokeMavenArchetype(String archetypeGroupId, String archetypeArtifactId,
      String archetypeVersion, Properties properties, File workingDirectory) {
    System.setProperty(MavenCli.MULTIMODULE_PROJECT_DIRECTORY, workingDirectory.getAbsolutePath());

    List<String> options = new LinkedList<>();
    options.addAll(Arrays.asList(new String[]{"archetype:generate", "-DinteractiveMode=false",
        "-DaskForDefaultPropertyValues=false", "-DarchetypeGroupId=" + archetypeGroupId,
        "-DarchetypeArtifactId=" + archetypeArtifactId, "-DarchetypeVersion=" + archetypeVersion}));
    properties.forEach((k, v) -> options.add("-D" + k + "=" + v));

    int result = new MavenCli().doMain(options.toArray(new String[0]),
        workingDirectory.getAbsolutePath(),
        System.out, System.err);
    try {
      final var resolve = workingDirectory.toPath().resolve("jakartaee-hello-world");
      generateMavenWrapper(resolve.toFile());
    } catch (IOException | InterruptedException e) {
      LOGGER.log(Level.SEVERE, "Unable to add maven wrapper", e);
      throw new RuntimeException(e);
    }
  }


  private static void generateMavenWrapper(File outputDirectory)
      throws IOException, InterruptedException {
    LOGGER.info("Adding Maven Wrapper to: " + outputDirectory);
    var pomFile = new File(outputDirectory, "pom.xml");
    boolean isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
    String starter = isWindows ? "cmd.exe" : "/bin/sh";
    String switcher = isWindows ? "/c" : "-c";
    String command = "mvn -f \"" + pomFile.getAbsolutePath() + "\" wrapper:wrapper";

    var processBuilder = new ProcessBuilder(starter, switcher, command);
    var process = processBuilder.start();
    var output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    process.waitFor();

    if (process.exitValue() != 0 || !output.contains("BUILD SUCCESS")) {
      System.out.println(output);
      FileUtils.forceDelete(outputDirectory);
      throw new RuntimeException("Failed to generate Maven wrapper.");
    }
  }
}
