package org.eclipse.starter.mavengenerator;

import java.util.Optional;
import java.util.Properties;

/**
 * Parameters to execute the archetype:generate Maven goal
 */
public class ArchetypeGenerateParameters extends MavenParameters {

    private Optional<String> archetypeGroupId = Optional.empty();
    private Optional<String> archetypeArtifactId = Optional.empty();
    private Optional<String> archetypeVersion = Optional.empty();
    private Optional<String> artifactGroupId = Optional.empty();
    private Optional<String> artifactArtifactId = Optional.empty();
    private Optional<String> artifactVersion = Optional.empty();

    public <THIS extends ArchetypeGenerateParameters> THIS archetypeGroupId(final String value) {
        this.archetypeGroupId = Optional.ofNullable(value);
        return (THIS)this;
    }

    public <THIS extends ArchetypeGenerateParameters> THIS archetypeArtifactId(final String value) {
        this.archetypeArtifactId = Optional.ofNullable(value);
        return (THIS)this;
    }

    public <THIS extends ArchetypeGenerateParameters> THIS archetypeVersion(final String value) {
        this.archetypeVersion = Optional.ofNullable(value);
        return (THIS)this;
    }

    public <THIS extends ArchetypeGenerateParameters> THIS groupId(final String value) {
        this.artifactGroupId = Optional.ofNullable(value);
        return (THIS)this;
    }

    public <THIS extends ArchetypeGenerateParameters> THIS artifactId(final String value) {
        this.artifactArtifactId = Optional.ofNullable(value);
        return (THIS)this;
    }

    public <THIS extends ArchetypeGenerateParameters> THIS version(final String value) {
        this.artifactVersion = Optional.ofNullable(value);
        return (THIS)this;
    }
    
    @Override
    protected Properties getProperties() {
        Properties properties = super.getProperties();
        archetypeGroupId.ifPresent(v -> properties.put("archetypeGroupId", v));
        archetypeArtifactId.ifPresent(v -> properties.put("archetypeArtifactId", v));
        archetypeVersion.ifPresent(v -> properties.put("archetypeVersion", v));
        artifactGroupId.ifPresent(v -> properties.put("groupId", v));
        artifactArtifactId.ifPresent(v -> properties.put("artifactId", v));
        artifactVersion.ifPresent(v -> properties.put("version", v));
        return properties;
    }

}

