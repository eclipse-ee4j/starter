import org.apache.commons.io.FileUtils

import java.nio.file.Files

// Processing target runtime specific code.
def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())
def jakartaVersion = request.properties["jakartaVersion"].trim()

switch (request.properties["runtime"])
{
    case "glassfish": println "Generating code for GlassFish"
                      FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                      if ( jakartaVersion != '8')  throw new RuntimeException("Jakarta version not available for this runtime yet")
                      break
                      
    case "tomee":     println "Generating code for TomEE"
                      if ( jakartaVersion != '8') FileUtils.deleteDirectory(new File(outputDirectory, "src/test"))
                      break

    default:          println "Generating code for Payara"
                      if ( jakartaVersion != '8') {
                          println "WARNING: Generating a non-production ready project with an alpha version of Payara"
                          FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                      }
}

//Replacing variables
bindJEEPrefix(jakartaVersion,outputDirectory)

private bindJEEPrefix(jakartaVersion, File outputDirectory) {
    def jeePackage = 'javax';
    if (jakartaVersion == '9') {
        jeePackage = 'jakarta'
    }

    def binding = ["jeePackage": jeePackage]
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