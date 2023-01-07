import java.nio.file.Files
import org.apache.commons.io.FileUtils

// Processing target runtime specific code.
def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())
def wildflyDirectory = new File(outputDirectory, "wildfly")

switch (request.properties["runtime"]) {
    case "glassfish": println "Generating code for GlassFish"
        FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
        break

    case "tomee": println "Generating code for TomEE"
        break

    case "wildfly": println "Generating code for WildFly"
        FileUtils.copyDirectory(wildflyDirectory, outputDirectory)
        break

    default: println "Generating code for Payara"
}

// We no longer need the wildfly directory, let's delete it.
FileUtils.forceDelete(new File(outputDirectory, "wildfly"))

// Jakarta version specific processing
def jakartaVersion = request.properties["jakartaVersion"].trim()
bindEEPackage(jakartaVersion, outputDirectory)

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

private bindEEPackage(jakartaVersion, File outputDirectory) {
    def eePackage = 'javax';
    if (jakartaVersion != '8') {
        eePackage = 'jakarta'
    }

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