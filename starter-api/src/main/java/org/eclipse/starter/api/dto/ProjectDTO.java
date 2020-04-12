package org.eclipse.starter.api.dto;

import java.util.Map;

public class ProjectDTO {

    private String artifactId;
    private String groupId;
    private String packageName;
    private String projectName;
    private Map<String, String> specifications;

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactid(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }
}
