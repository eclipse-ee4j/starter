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

package org.eclipse.starter.core.model;

import org.eclipse.starter.core.dependency.Dependency;
import org.eclipse.starter.core.template.generator.TemplateGenerator;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String artifactId;
    private String groupId;
    private String packageName;
    private String projectName;
    private List<String> specifications = new ArrayList<>();
    private String jakartaVersion;
    private List<Dependency> dependencies = new ArrayList<>();
    private List<TemplateGenerator> additionalTemplateGenerators = new ArrayList<>();

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

    public List<String> getSpecifications() {
        return specifications;
    }

    public String getJakartaVersion() {
        return jakartaVersion;
    }

    public void setJakartaVersion(String jakartaVersion) {
        this.jakartaVersion = jakartaVersion;
    }

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void addDependency(Dependency dependency) {
        this.dependencies.add(dependency);
    }

    public List<TemplateGenerator> getAdditionalTemplateGenerators() {
        return additionalTemplateGenerators;
    }

    public void addAdditionalTemplateGenerator(TemplateGenerator additionalTemplateGenerator) {
        this.additionalTemplateGenerators.add(additionalTemplateGenerator);
    }
}
