import org.apache.commons.io.FileUtils

// Processing target runtime specific code.
def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())
def glassFishDirectory = new File(outputDirectory, "glassfish")
def tomEEDirectory = new File(outputDirectory, "tomee")

def runtime = request.properties["runtime"].trim().toLowerCase()

switch (runtime)
{
    case "glassfish": println "Generating code for GlassFish"
                      FileUtils.copyDirectory(glassFishDirectory, outputDirectory)
                      FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                      break
                      
    case "tomee":     println "Generating code for TomEE"
                      FileUtils.copyDirectory(tomEEDirectory, outputDirectory)
                      break                      
                      
    default:          println "Generating code for Payara"
}

FileUtils.forceDelete(glassFishDirectory)
FileUtils.forceDelete(tomEEDirectory)

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