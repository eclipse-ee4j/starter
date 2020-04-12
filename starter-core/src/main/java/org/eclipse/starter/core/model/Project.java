package org.eclipse.starter.core.model;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String artifactId;
    private String groupId;
    private String packageName;
    private String projectName;
    private List<JakartaSpecification> jakartaSpecifications = new ArrayList<>();

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
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

    public List<JakartaSpecification> getJakartaSpecifications() {
        return jakartaSpecifications;
    }

    public void setJakartaSpecifications(List<JakartaSpecification> jakartaSpecifications) {
        this.jakartaSpecifications = jakartaSpecifications;
    }



    public void addJakartaSpecification(JakartaSpecification jakartaSpecification){
        jakartaSpecifications.add(jakartaSpecification);
    }
}
