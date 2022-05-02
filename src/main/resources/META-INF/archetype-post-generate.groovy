import org.apache.commons.io.FileUtils

def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())
def glassFishDirectory = new File(outputDirectory, "glassfish")
def tomEEDirectory = new File(outputDirectory, "tomee")
def jee9Directory = new File(outputDirectory,"jee9")

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

    case "jee9":      println "Generating Code for Jakarta EE 9"
                      FileUtils.copyDirectory(jee9Directory, outputDirectory)
                      break
                      
    default:          println "Generating code for Payara"
}

FileUtils.forceDelete(glassFishDirectory)
FileUtils.forceDelete(tomEEDirectory)
FileUtils.forceDelete(jee9Directory)
