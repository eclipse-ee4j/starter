import org.apache.commons.io.FileUtils

def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())
def glassFishDirectory = new File(outputDirectory, "glassfish")

def runtime = request.properties["runtime"].trim().toLowerCase()

switch (runtime)
{
    case "glassfish": println "Generating code for GlassFish"
                      FileUtils.copyDirectory(glassFishDirectory, outputDirectory)
                      FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
                      break
                      
    default:          println "Generating code for Payara"
}

FileUtils.forceDelete(glassFishDirectory)