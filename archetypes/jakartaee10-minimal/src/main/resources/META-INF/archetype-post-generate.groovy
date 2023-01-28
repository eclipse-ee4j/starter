import org.apache.commons.io.FileUtils

deprecated(new File(request.getOutputDirectory(), request.getArtifactId()))

private deprecated(File outputDirectory) {
    FileUtils.forceDelete(outputDirectory)
    throw new RuntimeException("Failed, this Archetype is deprecated in 2.0. Please use the jakarta-starter Archetype instead.")
}