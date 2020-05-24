/********************************************************************************
 * Copyright (c) 2020 Jeyvison Nascimento and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Jeyvison Nascimento - initial API and implementation
 ********************************************************************************/

package org.eclipse.starter.api.dto;

public class ProjectDTO {

    private String artifactId;
    private String groupId;
    private String packageName;
    private String projectName;
    private String[] specifications;

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

    public String[] getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String[] specifications) {
        this.specifications = specifications;
    }
}
