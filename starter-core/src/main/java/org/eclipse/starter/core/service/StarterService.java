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

package org.eclipse.starter.core.service;

import org.eclipse.starter.core.specification.handler.JAXRSHandler;
import org.eclipse.starter.core.specification.handler.JSONBHandler;
import org.eclipse.starter.core.specification.handler.JSONPHandler;
import org.eclipse.starter.core.specification.handler.SpecificationHandler;
import org.eclipse.starter.core.ThymeleafEngine;
import org.eclipse.starter.core.ZipCreator;
import org.eclipse.starter.core.model.Project;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class StarterService {

    public StarterService() {
        specificationHandlers = new HashMap<>();

        specificationHandlers.put("jax-rs", new JAXRSHandler());
        specificationHandlers.put("json-b", new JSONBHandler());
        specificationHandlers.put("json-p", new JSONPHandler());
    }

    @Inject
    private ThymeleafEngine thymeleafEngine;
    @Inject
    private ZipCreator zipCreator;

    private Map<String, SpecificationHandler> specificationHandlers;

    public byte[] generateArchive(
            String artifactId, String groupId, String projectName, String jakartaVersion) {

        Project project = populateModel(artifactId, groupId, projectName, jakartaVersion);

        Map<String, Object> variables = populateVariables(project);

        populateArchive(project, variables);

        return zipCreator.createArchive();

    }

    private void populateArchive(Project project, Map<String, Object> variables) {
        zipCreator.writeContents(project.getProjectName(), "pom.xml", thymeleafEngine.processFile("pom.xml.tpl", variables));

        SpecificationHandler specificationHandler = specificationHandlers.get("jax-rs");

        specificationHandler.handle(project, thymeleafEngine, variables, zipCreator);
    }


    private Map<String, Object> populateVariables(Project project) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("artifactId", project.getArtifactId());
        variables.put("groupId", project.getGroupId());
        variables.put("dependencies", project.getSpecifications());
        variables.put("packageName", project.getPackageName());
        variables.put("projectName", project.getProjectName());
        variables.put("jakartaVersion", project.getJakartaVersion());

        return variables;
    }

    private Project populateModel(
            String artifactId, String groupId, String projectName, String jakartaVersion) {

        Project project = new Project();

        if (artifactId == null || artifactId.trim().equals("")) {
            artifactId = "myproject";
        }

        project.setArtifactId(artifactId);

        if (groupId == null || groupId.trim().equals("")) {
            groupId = "com.myproject";
        }

        project.setGroupId(groupId);

        if (projectName == null || projectName.trim().equals("")) {
            projectName = "MyProject";
        }

        project.setProjectName(projectName);

        project.setPackageName(groupId + "." + artifactId);

        project.setJakartaVersion(jakartaVersion);

        return project;

    }
}
