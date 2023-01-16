package org.eclipse.starter.ui;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.starter.mavengenerator.CliMavenContext;
import org.eclipse.starter.mavengenerator.EeArchetypeGenerateParameters;
import org.eclipse.starter.mavengenerator.ZipCodec;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.ZipOutputStream;

@WebServlet(urlPatterns = {"/download.zip"}, name = "StarterServlet", loadOnStartup = 1)
public class StarterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(
            MethodHandles.lookup().lookupClass().getName());
    private static final String DOWNLOADABLE_FILE_NAME = "jakartaee-project.zip";
    private static final String ZIP_EXTENSION = ".zip";


    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws IOException {
        logger.info("Generating project from archetype");

        final Parameters parametersFromrequest = this.parametersFromrequest(req);

        logger.fine(() -> String.format("Archetype properties: archetypeGroupId=%s, archetypeArtifactId=%s, "
                        + "archetypeVersion=%s, profile=%s, groupId=%s, artifactId=%s, version=%s",
                parametersFromrequest.archetypeGroupId, parametersFromrequest.archetypeArtifactId,
                parametersFromrequest.archetypeVersion, parametersFromrequest.profile,
                parametersFromrequest.groupId, parametersFromrequest.artifactId, parametersFromrequest.version));

        final var downloadableFileName = this.isEmpty(parametersFromrequest.artifactId)
                ? DOWNLOADABLE_FILE_NAME
                : parametersFromrequest.artifactId + ZIP_EXTENSION;

        this.prepareResponse(resp, downloadableFileName);
        final Path generatedDirectory = this.generateProjectFromArchetype(parametersFromrequest);
        try (final var fileStream = Files.list(generatedDirectory)) {
            final Path projectDirectory = fileStream.findAny().orElseThrow(() -> new IllegalStateException("Generated project directory not found"));
            this.zipDirectoryToOutput(projectDirectory, resp.getOutputStream());
        } finally {
            this.deleteDirectoryRecursively(generatedDirectory);
        }
    }

    private void prepareResponse(final HttpServletResponse resp, final String filename) {
        resp.setContentType("application/zip");
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
    }

    // this method is synchronized because I'm not sure whether the Maven CLI is thread safe
    private synchronized Path generateProjectFromArchetype(final Parameters p) throws IOException {
        final Path targetDirectory = Files.createTempDirectory("ee-starter-maven-project-");

        new EeArchetypeGenerateParameters()
                .profile(p.profile)
                .archetypeGroupId(p.archetypeGroupId)
                .archetypeArtifactId(p.archetypeArtifactId)
                .archetypeVersion(p.archetypeVersion)
                .groupId(p.groupId)
                .artifactId(p.artifactId)
                .version(p.version)
                .interactiveMode(false)
                .addGoal("org.apache.maven.plugins:maven-archetype-plugin:3.2.1:generate")
                .updateMavenContext(new CliMavenContext()
                        .workingDirectory(targetDirectory)
                        .addOptions("-X")
                )
                .run();

        return targetDirectory;
    }

    private boolean isEmpty(final String input) {
        return input == null || input.isEmpty();
    }

    private Parameters parametersFromrequest(final HttpServletRequest req) {
        final Parameters p = new Parameters();
        p.archetypeGroupId = req.getParameter("archetypeGroupId");
        p.archetypeArtifactId = req.getParameter("archetypeArtifactId");
        p.archetypeVersion = req.getParameter("archetypeVersion");

        this.requireNotNull(p.archetypeGroupId, p.archetypeArtifactId, p.archetypeVersion);

        p.groupId = this.getParameterOrDefaultValue(req, "groupId", "com.example");
        p.artifactId = this.getParameterOrDefaultValue(req, "artifactId", "demo");
        p.version = this.getParameterOrDefaultValue(req, "version", "1.0.0-SNAPSHOT");
        p.profile = this.getParameterOrDefaultValue(req, "profile", "api");

        return p;
    }

    private void requireNotNull(final String archetypeGroupId, final String archetypeArtifactId, final String archetypeVersion) {
        Stream.of(new String[]{"archetypeGroupId", archetypeGroupId},
                        new String[]{"archetypeArtifactId", archetypeArtifactId},
                        new String[]{"archetypeVersion", archetypeVersion}
                )
                .filter(entry -> entry[1] == null)
                .findAny()
                .ifPresent(entry -> {
                    throw new RuntimeException("Query parameter " + entry[0] + " must be provided.");
                });
    }

    private String getParameterOrDefaultValue(final HttpServletRequest req, final String parameterName, final String defaultValue) {
        final String paramValue = req.getParameter(parameterName);
        return paramValue != null ? paramValue : defaultValue;
    }

    private void zipDirectoryToOutput(final Path generatedDirectory, final ServletOutputStream outputStream) throws IOException {
        try (final ZipOutputStream zipOutStream = new ZipOutputStream(outputStream)) {
            new ZipCodec()
                    .addDirToZipArchive(generatedDirectory, zipOutStream);
        }
    }

    private void deleteDirectoryRecursively(final Path rootDirectory) throws IOException {
        try (final var fileStream = Files.walk(rootDirectory)) {
            //noinspection ResultOfMethodCallIgnored
            fileStream.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    private static class Parameters {
        String archetypeGroupId = null;
        String archetypeArtifactId = null;
        String archetypeVersion = null;
        String groupId = null;
        String artifactId = null;
        String version = null;
        String profile = null;
    }
}
