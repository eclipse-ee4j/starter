import org.apache.commons.io.FileUtils

def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())
def glassFishDirectory = new File(outputDirectory, "glassfish")
def wildflyDirectory = new File(outputDirectory, "wildfly")

def runtime = request.properties["runtime"].trim().toLowerCase()

switch (runtime)
{
    case "glassfish": println "Generating code for GlassFish"
                      FileUtils.copyDirectory(glassFishDirectory, outputDirectory)
                      FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                      break

    case "wildfly": println "Generating code for wildfly"
                    FileUtils.copyDirectory(wildflyDirectory, outputDirectory)
                    FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                    break;
    default: println "Generating code for Payara"
}

FileUtils.forceDelete(glassFishDirectory)
FileUtils.forceDelete(wildflyDirectory)
