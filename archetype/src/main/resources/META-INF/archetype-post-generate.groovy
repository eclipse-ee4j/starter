import java.nio.file.Files
import org.apache.commons.io.FileUtils

// Processing target runtime specific code.
def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())

switch (request.properties["runtime"]) {
    case "glassfish": println "Generating code for GlassFish"
        if (request.properties["docker"].equalsIgnoreCase("yes")) {
           println "WARNING: GlassFish does not support Docker"
        }
        
        FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
        break

    case "tomee": println "Generating code for TomEE"
        break

    case "payara": println "Generating code for Payara"
        break

    default: println "No runtime will be included in the sample"
}

// Jakarta version specific processing
def jakartaVersion = request.properties["jakartaVersion"].trim()
bindEEPackage(jakartaVersion, outputDirectory)

// Remove Dockerfile if not requested or possible
if (request.properties["docker"].equalsIgnoreCase("no")) {
    println "Docker support was not requested"
    FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
} else if (request.properties["runtime"].equalsIgnoreCase("none")) {
    println "WARNING: Docker support is not possible without choosing a runtime"
    FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))    
}

// Adding Maven Wrapper
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
    throw new RuntimeException("Failed to generate code from archetype.")
}

println "The README.md file in the " + request.properties["artifactId"] + " directory explains how to run the generated application"

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