package org.eclipse.starter.core.dependency;

public class JakartaEE8Dependency implements Dependency {
    @Override
    public String getGroupId() {
        return "jakarta.platform";
    }

    @Override
    public String getArtifactId() {
        return "jakarta.jakartaee-api";
    }

    @Override
    public String getVersion() {
        return "8.0.0";
    }

    @Override
    public String getType() {
        return "jar";
    }

    @Override
    public String getScope() {
        return "provided";
    }
}
