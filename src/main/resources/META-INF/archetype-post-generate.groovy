import org.apache.commons.io.FileUtils

def outputDirectory = new File(request.getOutputDirectory(), request.getArtifactId())
def glassFishDirectory = new File(outputDirectory, "glassfish")
def tomEEDirectory = new File(outputDirectory, "tomee")

def runtime = request.properties["runtime"].trim().toLowerCase()
def jakartaVersion = request.properties["jakartaVersion"].trim()

switch (runtime) {
    case "glassfish": println "Generating code for GlassFish"
        FileUtils.copyDirectory(glassFishDirectory, outputDirectory)
        FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
        break

    case "tomee": println "Generating code for TomEE"
        FileUtils.copyDirectory(tomEEDirectory, outputDirectory)
        break

    default: println "Generating code for Payara"
        if ("9" == jakartaVersion) FileUtils.forceDelete(new File(outputDirectory, "Dockerfile"))
}

FileUtils.forceDelete(glassFishDirectory)
FileUtils.forceDelete(tomEEDirectory)

bindJEEPrefix(jakartaVersion, outputDirectory)


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
                }
            }
        }
    }
}
