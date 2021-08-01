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

import org.eclipse.starter.core.dependency.JakartaEE8Dependency;
import org.eclipse.starter.core.dependency.JakartaEE9Dependency;
import org.eclipse.starter.core.plugin.JavaVersionPlugin;
import org.eclipse.starter.core.plugin.LibertyPlugin;
import org.eclipse.starter.core.plugin.PayaraPlugin;
import org.eclipse.starter.core.template.generator.JAXRSTemplateGenerator;
import org.eclipse.starter.core.template.generator.JSONBTemplateGenerator;
import org.eclipse.starter.core.template.generator.JSONPTemplateGenerator;
import org.eclipse.starter.core.template.generator.TemplateGenerator;
import org.eclipse.starter.core.ThymeleafEngine;
import org.eclipse.starter.core.ZipCreator;
import org.eclipse.starter.core.model.Project;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class StarterService {

    public StarterService() {
        specificationHandlers = new HashMap<>();

        specificationHandlers.put("jax-rs", new JAXRSTemplateGenerator());
        specificationHandlers.put("json-b", new JSONBTemplateGenerator());
        specificationHandlers.put("json-p", new JSONPTemplateGenerator());
    }

    @Inject
    private ThymeleafEngine thymeleafEngine;
    @Inject
    private ZipCreator zipCreator;

    private final Map<String, TemplateGenerator> specificationHandlers;

    public byte[] generateArchive(
            String artifactId, String groupId, String jakartaVersion, String container) {

        Project project = populateModel(artifactId, groupId, jakartaVersion, container);

        Map<String, Object> variables = populateVariables(project);

        populateArchive(project, variables);

        return zipCreator.createArchive();

    }

    private void populateArchive(Project project, Map<String, Object> variables) {

        TemplateGenerator templateGenerator = specificationHandlers.get("jax-rs");

        templateGenerator.generate(project, thymeleafEngine, variables, zipCreator);

        zipCreator.writeContents(project.getProjectName(), "pom.xml", thymeleafEngine.processFile("pom.xml.tpl", variables));
    }


    private Map<String, Object> populateVariables(Project project) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("artifactId", project.getArtifactId());
        variables.put("groupId", project.getGroupId());
        variables.put("dependencies",project.getDependencies());
        variables.put("packageName", project.getPackageName());
        variables.put("projectName", project.getProjectName());
        variables.put("jakartaVersion", project.getJakartaVersion());
        variables.put("jakartaPackage", project.getJakartaVersion().startsWith("9") ? "jakarta" : "javax");
        variables.put("plugins", List.of(new JavaVersionPlugin(), new PayaraPlugin()));

        return variables;
    }

    private Project populateModel(
            String artifactId, String groupId, String jakartaVersion, String container) {

        Project project = new Project();

        if (artifactId == null || artifactId.trim().equals("")) {
            artifactId = "myproject";
        }

        project.setArtifactId(artifactId);

        if (groupId == null || groupId.trim().equals("")) {
            groupId = "com.myproject";
        }

        if(jakartaVersion.startsWith("8")){
            project.addDependency(new JakartaEE8Dependency());
        }else{
            project.addDependency(new JakartaEE9Dependency());
        }

//        if ("payara".equals(container)) {
//            project.addDependency(new PayaraContainerDependency());
//            project.addAdditionalTemplateGenerator(new PayaraTemplateGenerator());
//        }

        project.setGroupId(groupId);

        project.setProjectName(artifactId);

        project.setPackageName(groupId + "." + artifactId);

        project.setJakartaVersion(jakartaVersion);

        return project;

    }
}
