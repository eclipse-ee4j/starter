import java.nio.file.Files
import org.apache.commons.io.FileUtils

def jakartaVersion = request.properties["jakartaVersion"].trim()
def profile = request.properties["profile"].trim()
def javaVersion = request.properties["javaVersion"].trim()
def runtime = request.properties["runtime"].trim()
def docker = request.properties["docker"].trim()
def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())

validateInput(jakartaVersion, javaVersion, runtime, profile, outputDirectory)
generateRuntime(runtime, jakartaVersion, docker, outputDirectory)
bindEEPackage(jakartaVersion, outputDirectory)
generateDocker(docker, runtime, outputDirectory)
generateMavenWrapper(outputDirectory)
printSummary()

private validateInput(jakartaVersion, javaVersion, runtime, profile, File outputDirectory){
    if (profile.equalsIgnoreCase("core") && jakartaVersion != '10') {
       FileUtils.forceDelete(outputDirectory)
       throw new RuntimeException("Failed, the Core Profile is only supported for Jakarta EE 10")
    }

    if (runtime.equalsIgnoreCase("payara") && (jakartaVersion != '8') && (javaVersion == '8')) {
       FileUtils.forceDelete(outputDirectory)
       throw new RuntimeException("Failed, Payara 6 does not support Java SE 8")
    }    

    if (runtime.equalsIgnoreCase("glassfish") && profile.equalsIgnoreCase("core")) {
       FileUtils.forceDelete(outputDirectory)
       throw new RuntimeException("Failed, GlassFish does not support the Core Profile")
    }

    if (runtime.equalsIgnoreCase("glassfish") && (jakartaVersion != '8') && (javaVersion == '8')) {
       FileUtils.forceDelete(outputDirectory)
       throw new RuntimeException("Failed, GlassFish 7 does not support Java SE 8")
    }

    if (runtime.equalsIgnoreCase("tomee") && (jakartaVersion == '10')) {
       FileUtils.forceDelete(outputDirectory)
       throw new RuntimeException("Failed, TomEE does not support Jakarta EE 10")
    }

    if (runtime.equalsIgnoreCase("tomee") && !profile.equalsIgnoreCase("web")) {
       FileUtils.forceDelete(outputDirectory)
       throw new RuntimeException("Failed, TomEE does not support the full and Core Profiles")
    }

    if (runtime.equalsIgnoreCase("tomee") && (jakartaVersion != '8') && (javaVersion == '8')) {
       FileUtils.forceDelete(outputDirectory)
       throw new RuntimeException("Failed, TomEE 9 does not support Java SE 8")
    }    
}

private generateRuntime(runtime, jakartaVersion, docker, File outputDirectory) {
    switch (runtime) {
        case "glassfish": println "Generating code for GlassFish"
            if (docker.equalsIgnoreCase("yes")) {
                println "WARNING: GlassFish does not support Docker"
                FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
            }
        
            break

        case "tomee": println "Generating code for TomEE"
            if (jakartaVersion != '8') {
                println "WARNING: TomEE 9 is not yet a production ready release"
            }

            break

        case "payara": println "Generating code for Payara"
            if ((jakartaVersion != '8') && docker.equalsIgnoreCase("yes")) {
                println "WARNING: Payara does not yet support Docker for this Jakarta EE version"
                FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
            }
            
            break

        default: println "No runtime will be included in the sample"
    }
}

private bindEEPackage(jakartaVersion, File outputDirectory) {
    def eePackage = 'javax';
    if (jakartaVersion != '8') {
        eePackage = 'jakarta'
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

private generateMavenWrapper(File outputDirectory) {
    println "Adding Maven Wrapper"

    def pomFile = new File(outputDirectory, "pom.xml")
    def isWindows = System.properties['os.name'].toLowerCase().contains('windows')
    def starter = isWindows ? "cmd.exe" : "/bin/sh"
    def switcher = isWindows ? "/c" : "-c"
    def command = "mvn -f \"${pomFile.getAbsolutePath()}\" wrapper:wrapper"

    def output = new StringBuilder()
    def proc = [starter, switcher, command].execute();
    proc.consumeProcessOutput(output, output)
    proc.waitFor()

    if (proc.exitValue() != 0 || output == null || !output.contains("BUILD SUCCESS")) {
        println("${output}")
        FileUtils.forceDelete(outputDirectory)    
        throw new RuntimeException("Failed to generate code from archetype.")
    }
}

private printSummary(){
    println "The README.md file in the " + request.properties["artifactId"] + " directory explains how to run the generated application"
}