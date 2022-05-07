import org.apache.commons.io.FileUtils

import java.nio.file.Files

def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())
def glassFishDirectory = new File(outputDirectory, "glassfish")
def tomEEDirectory = new File(outputDirectory, "tomee")

def runtime = request.properties["runtime"].trim().toLowerCase()
def jakartaVersion = request.properties["jakartaVersion"].trim()

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
                      if ("9" == jakartaVersion) FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))

}

FileUtils.forceDelete(glassFishDirectory)
FileUtils.forceDelete(tomEEDirectory)