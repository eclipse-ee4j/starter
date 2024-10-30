import org.apache.commons.io.FileUtils

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

def jakartaVersion = request.properties["jakartaVersion"].trim()
def profile = request.properties["profile"].trim()
def javaVersion = request.properties["javaVersion"].trim()
def runtime = request.properties["runtime"].trim()
def docker = request.properties["docker"].trim()

def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())

validateInput(jakartaVersion, profile, javaVersion, runtime, docker, outputDirectory)
generateRuntime(runtime, jakartaVersion, docker, outputDirectory)
bindEEPackage(jakartaVersion, outputDirectory)
generateDocker(docker, runtime, outputDirectory)
chmod(outputDirectory.toPath().resolve("mvnw").toFile())
printSummary()

private validateInput(jakartaVersion, profile, javaVersion, runtime, docker, File outputDirectory) {
    if ((jakartaVersion != '8') && (jakartaVersion != '9')
            && (jakartaVersion != '9.1') && (jakartaVersion != '10')) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Jakarta EE versions are 8, 9, 9.1, and 10")
    }

    if (!["core", "web", "full"].contains(profile.toLowerCase())) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Jakarta EE profiles are core, web, and full")
    }

    if (!['8', '11', '17', '21'].contains(javaVersion)) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Java SE versions are 8, 11, and 17")
    }

    if (!["none", "glassfish", "open-liberty", "payara", "tomee", "wildfly", "helidon"].contains(runtime.toLowerCase())) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid runtime values are none, glassfish, open-liberty, payara, tomee, and wildfly")
    }

    if (!docker.equalsIgnoreCase("yes") && !docker.equalsIgnoreCase("no")) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Docker options are yes and no")
    }

    if (profile.equalsIgnoreCase("core") && jakartaVersion != '10') {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, the Core Profile is only supported for Jakarta EE 10")
    }

    if ((javaVersion == '8') && (jakartaVersion == '10')) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, Jakarta EE 10 does not support Java SE 8")
    }

    if (runtime.equalsIgnoreCase("payara") && (jakartaVersion != '8') && (javaVersion == '8')) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, Payara 6 does not support Java SE 8")
    }

    if (runtime.equalsIgnoreCase("glassfish")) {
        if (profile.equalsIgnoreCase("core")) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, GlassFish does not support the Core Profile")
        }

        if ((jakartaVersion != '8') && (javaVersion == '8')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, GlassFish 7 does not support Java SE 8")
        }
    }

    if (runtime.equalsIgnoreCase("tomee")) {
        if (jakartaVersion == '10') {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, TomEE does not yet support Jakarta EE 10")
        }

        if (jakartaVersion == '9') {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, TomEE is certified against Jakarta EE 9.1, but not Jakarta EE 9")
        }

        if (!profile.equalsIgnoreCase("web")) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, TomEE does not support the full and Core Profiles")
        }

        if ((jakartaVersion != '8') && (javaVersion == '8')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, TomEE 9 does not support Java SE 8")
        }
    }

    if (runtime.equalsIgnoreCase("wildfly")) {
        if ((jakartaVersion == '9') || (jakartaVersion == '9.1')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, WildFly does not offer a release for Jakarta EE 9 or Jakarta EE 9.1")
        }
    }

    if (runtime.equalsIgnoreCase("helidon")) {
        if (jakartaVersion != '10' && javaVersion != '21' && profile != 'core') {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, Helidon offer a release for Jakarta EE 10 Core profile only")
        }
    }
}

private generateRuntime(runtime, jakartaVersion, docker, File outputDirectory) {
    switch (runtime) {
        case "glassfish": println "Generating code for GlassFish"
            if (docker.equalsIgnoreCase("yes")) {
                println "WARNING: GlassFish does not yet support Docker"
                FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
            }

            break

        case "tomee": println "Generating code for TomEE"
            break

        case "payara": println "Generating code for Payara"
            break

        case "wildfly": println "Generating code for WildFly"
            break

        case "open-liberty": println "Generating code for Open Liberty"
            break

        case "helidon": println "Generating code for Helidon"
            var helidonDir = new File(outputDirectory, "src/main/helidon")
            var resourcesDir = new File(outputDirectory, "src/main/resources")
            var webappDir = new File(outputDirectory,"src/main/webapp");
            FileUtils.forceDelete(new File(webappDir, "/WEB-INF"))
            FileUtils.copyDirectory(new File(helidonDir, "resources"), resourcesDir)
            FileUtils.copyDirectory(webappDir, new File(resourcesDir, "/WEB"))
            FileUtils.forceDelete(webappDir)
            FileUtils.forceDelete(helidonDir)
            break

        default: println "No runtime will be included in the sample"
    }

    if (!runtime.equalsIgnoreCase("open-liberty")) {
        // We do not need the liberty configuration directory, let's delete it.
        FileUtils.forceDelete(new File(outputDirectory, "src/main/liberty"))
    }

    if (!runtime.equalsIgnoreCase("helidon")) {
        // We do not need the helidon configuration directory, let's delete it.
        FileUtils.forceDelete(new File(outputDirectory, "src/main/helidon"))
    }
}

static void bindEEPackage(String jakartaVersion, File outputDirectory) throws IOException {
    String eePackage = "jakarta"
    if ("8".equals(jakartaVersion)) {
        eePackage = "javax"
    }

    println "Binding EE package: $eePackage"

    File[] files = outputDirectory.listFiles()
    if (files != null) {
        files.each { file ->
            traverseFiles(file, eePackage)
        }
    }
}

private static void traverseFiles(File file, String eePackage) throws IOException {
    if (file.isDirectory()) {
        File[] files = file.listFiles()
        if (files != null) {
            files.each { subFile ->
                traverseFiles(subFile, eePackage)
            }
        }
    } else if (file.isFile() && file.getName().matches(".*\\.(xml|java)") && !file.getName().endsWith("pom.xml")) {
        processFile(file, eePackage)
    }
}

private static void processFile(File file, String eePackage) throws IOException {
    Path filePath = file.toPath()
    String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8)
    String replacedContent = content.replaceAll('\\$\\{eePackage}', eePackage)

    Files.write(filePath, replacedContent.getBytes(StandardCharsets.UTF_8))
}

private generateDocker(docker, runtime, File outputDirectory) {
    if (docker.equalsIgnoreCase("no")) {
        println "Docker support was not requested"
        FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
    } else if (runtime.equalsIgnoreCase("none")) {
        println "WARNING: Docker support is not possible without choosing a runtime"
        FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
    }
}

private chmod(File mvnw) {
    def isWindows = System.properties['os.name'].toLowerCase().contains('windows')

    if (!isWindows) {
        println "Running chmod on " + mvnw.getName()

        def processBuilder = new ProcessBuilder("chmod", "+x", mvnw.getAbsolutePath())
        def process = processBuilder.start()
        def exitCode = process.waitFor()

        if (exitCode != 0) {
            println "WARNING: Failed to set executable permission on file: " + mvnw.getAbsolutePath()
        }
    }
}

private printSummary() {
    println "The README.md file in the " + request.properties["artifactId"] + " directory explains how to run the generated application"
}
