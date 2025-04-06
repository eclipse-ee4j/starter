import org.apache.commons.io.FileUtils

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

def jakartaVersion = request.properties["jakartaVersion"].trim()
def profile = request.properties["profile"].trim().toLowerCase()
def javaVersion = request.properties["javaVersion"].trim()
def runtime = request.properties["runtime"].trim().toLowerCase()
def docker = request.properties["docker"].trim().toLowerCase()

def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())

validateInput(jakartaVersion, profile, javaVersion, runtime, docker, outputDirectory)
generateRuntime(runtime, jakartaVersion, profile, javaVersion, docker, outputDirectory)
bindEEPackage(jakartaVersion, outputDirectory)
generateWebApp(profile, outputDirectory)
generateDocker(docker, runtime, outputDirectory)
chmod(outputDirectory.toPath().resolve("mvnw").toFile())
printSummary()

private validateInput(jakartaVersion, profile, javaVersion, runtime, docker, File outputDirectory) {
    if (!(jakartaVersion in ['8', '9', '9.1', '10', '11'])) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Jakarta EE versions are 8, 9, 9.1, 10, and 11")
    }

    if (!(profile in ['core', 'web', 'full'])) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Jakarta EE profiles are core, web, and full")
    }

    if (!(javaVersion in ['8', '11', '17', '21'])) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Java SE versions are 8, 11, 17, and 21")
    }

    if (!(runtime in ['none', 'glassfish', 'open-liberty', 'payara', 'tomee', 'wildfly'])) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid runtime values are none, glassfish, open-liberty, payara, tomee, and wildfly")
    }

    if (!(docker in ['yes', 'no'])) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Docker options are yes and no")
    }

    // As EE 11 progresses, this check should be removed.
    if ((profile == 'full') && (jakartaVersion == '11')) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, for Jakarta EE 11 please choose Core or Web Profile")
    }

    if ((profile == 'core') && (Double.valueOf(jakartaVersion) < 10)) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, the Core Profile is only supported for Jakarta EE 10 and above")
    }

    if ((javaVersion == '8') && (Double.valueOf(jakartaVersion) > 9.1)) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, Java SE 8 is not supported in Jakarta EE versions 10 or later")
    }

    if ((javaVersion == '11') && (Double.valueOf(jakartaVersion) > 10)) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, Jakarta EE 11 does not support Java SE 11")
    }

    if (runtime == 'glassfish') {
        if (profile == 'core') {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, GlassFish does not support the Core Profile")
        }

        if ((Integer.valueOf(javaVersion) > 8) && (jakartaVersion == '8')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, GlassFish 5 only supports Java SE 8")
        }
    }

    if (runtime == 'open-liberty') {
        // As EE 11 progresses, this check should be removed.
        if ((jakartaVersion == '11') && (profile == 'web')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, Open Liberty does not yet support the Jakarta EE 11 Web Profile")
        }
    }

    if (runtime == 'payara') {
        // As EE 11 progresses, this check should be removed.
        if (jakartaVersion == '11') {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, Payara does not yet support Jakarta EE 11")
        }

        if ((jakartaVersion == '9') || (jakartaVersion == '9.1')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, Payara does not offer a stable release for Jakarta EE 9 or Jakarta EE 9.1")
        }
    }

    if (runtime == 'tomee') {
        if (Double.valueOf(jakartaVersion) > 9.1) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, TomEE does not yet support Jakarta EE versions 10 or later")
        }

        if (jakartaVersion == '9') {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, TomEE is certified against Jakarta EE 9.1, but not Jakarta EE 9")
        }

        if (profile != 'web') {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, TomEE only supports the Web Profile")
        }

        if ((jakartaVersion != '8') && (javaVersion == '8')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, TomEE 9 does not support Java SE 8")
        }
    }

    if (runtime == 'wildfly') {
        // As EE 11 progresses, this check should be removed.
        if ((jakartaVersion == '11') && (profile == 'web')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, WildFly does not yet support the Jakarta EE 11 Web Profile")
        }
		
        if ((jakartaVersion == '9') || (jakartaVersion == '9.1')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, WildFly does not offer a stable release for Jakarta EE 9 or Jakarta EE 9.1")
        }
    }
}

private generateRuntime(runtime, jakartaVersion, profile, javaVersion, docker, File outputDirectory) {
    switch (runtime) {
        case "glassfish": println "Generating code for GlassFish"
            if (docker.equalsIgnoreCase("yes")) {
                if (Double.valueOf(jakartaVersion) != 10) {
                    println "WARNING: GlassFish only supports Docker for Jakarta EE 10"
                    FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                } else if (javaVersion != '17') {
                    println "WARNING: GlassFish only supports Docker for Java 17"
                    FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                } else if (profile != 'full') {
                    println "WARNING: GlassFish only supports Docker for the full platform"
                    FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                }
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

        default: println "No runtime will be included in the sample"
    }

    if (runtime != 'open-liberty') {
        FileUtils.forceDelete(new File(outputDirectory, "src/main/liberty"))
    }
}

static void bindEEPackage(String jakartaVersion, File outputDirectory) throws IOException {
    String eePackage = "jakarta"
    if (jakartaVersion == '8') {
        eePackage = "javax"
    }

    println "Binding EE package: $eePackage"

    File[] files = outputDirectory.listFiles()
    if (files != null) {
        files.each { file -> traverseFiles(file, eePackage) }
    }
}

private static void traverseFiles(File file, String eePackage) throws IOException {
    if (file.isDirectory()) {
        File[] files = file.listFiles()
        if (files != null) {
            files.each { subFile -> traverseFiles(subFile, eePackage) }
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

private generateWebApp(profile, File outputDirectory) {
    if (profile.equalsIgnoreCase("core")) {
        println "Core profile doesn't support Jakarta Servlet"
        FileUtils.forceDelete(new File(outputDirectory, "src/main/webapp/"))
    }
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
