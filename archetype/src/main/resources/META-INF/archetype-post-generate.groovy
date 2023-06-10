import org.apache.commons.io.FileUtils

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

    if (!profile.equalsIgnoreCase("core") && !profile.equalsIgnoreCase("web") && !profile.equalsIgnoreCase("full")) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Jakarta EE profiles are core, web, and full")
    }

    if ((javaVersion != '8') && (javaVersion != '11') && (javaVersion != '17')) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Java SE versions are 8, 11, and 17")
    }

    if (!runtime.equalsIgnoreCase("none") && !runtime.equalsIgnoreCase("glassfish")
           && !runtime.equalsIgnoreCase("open-liberty") && !runtime.equalsIgnoreCase("payara")
           && !runtime.equalsIgnoreCase("tomee") && !runtime.equalsIgnoreCase("wildfly")) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid runtimes are none, GlassFish, Open-Liberty, Payara, TomEE, and WildFly")
    }

    if (!docker.equalsIgnoreCase("yes") && !docker.equalsIgnoreCase("no")) {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, valid Docker options are Yes and No")
    }

    if (profile.equalsIgnoreCase("core") && jakartaVersion != '10') {
        FileUtils.forceDelete(outputDirectory)
        throw new RuntimeException("Failed, the Core Profile is only supported for Jakarta EE 10")
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

        if ((jakartaVersion == '10') && (javaVersion == '8')) {
            FileUtils.forceDelete(outputDirectory)
            throw new RuntimeException("Failed, WildFly 27 does not support Java SE 8")
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
            if (jakartaVersion != '8') {
                println "WARNING: TomEE 9 is not yet a production ready release"
            }

            break

        case "payara": println "Generating code for Payara"
            break

        case "wildfly": println "Generating code for WildFly"
            break

        case "open-liberty": println "Generating code for Open Liberty"
            break

        default: println "No runtime will be included in the sample"
    }

    if (!runtime.equalsIgnoreCase("open-liberty")) {
        // We do not need the liberty configuration directory, let's delete it.
        FileUtils.forceDelete(new File(outputDirectory, "src/main/liberty"))
    }
}

private bindEEPackage(jakartaVersion, File outputDirectory) {
    def eePackage = 'jakarta';
    if (jakartaVersion == '8') {
        eePackage = 'javax'
    }

    println "Binding EE package: " + eePackage

    def binding = ["eePackage": eePackage]
    def engine = new groovy.text.SimpleTemplateEngine()

    outputDirectory.traverse(type: groovy.io.FileType.FILES, nameFilter: ~/.*\.(xml|java)$/) { it ->
        if (!it.name.endsWith("pom.xml")) {
            it.withReader('UTF-8') { reader ->
                try {
                    def template = engine.createTemplate(reader).make(binding)
                    new FileWriter(it).write(template)
                } catch (ignored) {
                    println ignored
                }
            }
        }
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
