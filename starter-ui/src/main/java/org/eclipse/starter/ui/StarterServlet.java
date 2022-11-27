package org.eclipse.starter.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.zip.ZipOutputStream;
import org.eclipse.starter.mavengenerator.CliMavenContext;
import org.eclipse.starter.mavengenerator.EeArchetypeGenerateParameters;
import org.eclipse.starter.mavengenerator.ZipCodec;

@WebServlet(urlPatterns = {"/download.zip"}, name = "StarterServlet")
public class StarterServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(
            MethodHandles.lookup().lookupClass().getName());
    private static final String DOWNLOADABLE_FILE_NAME = "jakartaee-cafe.zip";

    private static class Parameters {
        String archetypeGroupId = null;
        String archetypeArtifactId = null;
        String archetypeVersion = null;
        String groupId = null;
        String artifactId = null;
        String version = null;
        String profile = null;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        logger.info("Generating project from archetype");

        Parameters p = parametersFromrequest(req);

        logger.fine(() -> String.format("Archetype properties: archetypeGroupId=%s, archetypeArtifactId=%s, "
                + "archetypeVersion=%s, profile=%s, groupId=%s, artifactId=%s, version=%s", 
                p.archetypeGroupId, p.archetypeArtifactId, p.archetypeVersion, p.profile,
                p.groupId, p.artifactId, p.version));

        prepareResponse(resp, DOWNLOADABLE_FILE_NAME);
        Path generatedDirectory = generateProjectFromArchetype(p);
        try {
            Path projectDirectory = Files.list(generatedDirectory).findAny().get();
            zipDirectoryToOutput(projectDirectory, resp.getOutputStream());
        } finally {
            deleteDirectoryRecursively(generatedDirectory);
        }
    }

    private void prepareResponse(HttpServletResponse resp, String filename) throws IOException {
        resp.setContentType("application/zip");
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
    }

    // this method is synchronized because I'm not sure whether the Maven CLI is thread safe
    private synchronized Path generateProjectFromArchetype(Parameters p) throws IOException {
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

    private Parameters parametersFromrequest(HttpServletRequest req) {
        Parameters p = new Parameters();
        p.archetypeGroupId = req.getParameter("archetypeGroupId");
        p.archetypeArtifactId = req.getParameter("archetypeArtifactId");
        p.archetypeVersion = req.getParameter("archetypeVersion");

        requireNotNull(p.archetypeGroupId, p.archetypeArtifactId, p.archetypeVersion);

        p.groupId = getParameterOrDefaultValue(req, "groupId", "com.example");
        p.artifactId = getParameterOrDefaultValue(req, "artifactId", "demo");
        p.version = getParameterOrDefaultValue(req, "version", "1.0.0-SNAPSHOT");
        p.profile = getParameterOrDefaultValue(req, "profile", "api");
        
        return p;
    }

    private void requireNotNull(String archetypeGroupId, String archetypeArtifactId, String archetypeVersion) {
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

    private String getParameterOrDefaultValue(HttpServletRequest req, String parameterName, String defaultValue) {
        final String paramValue = req.getParameter(parameterName);
        return paramValue != null ? paramValue : defaultValue;
    }

    private void zipDirectoryToOutput(Path generatedDirectory, ServletOutputStream outputStream) throws IOException {
        try (ZipOutputStream zipOutStream = new ZipOutputStream(outputStream)) {
            new ZipCodec()
                    .addDirToZipArchive(generatedDirectory, zipOutStream);
        }
    }

    private void deleteDirectoryRecursively(Path rootDirectory) throws IOException {
        Files.walk(rootDirectory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

    }
}
