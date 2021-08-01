package org.eclipse.starter.core.dependency;

public interface Dependency {
    String getGroupId();

    String getArtifactId();

    String getVersion();

    String getType();

    String getScope();
}
