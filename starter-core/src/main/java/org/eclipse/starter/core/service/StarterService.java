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

import org.eclipse.starter.core.JakartaSpecOption;
import org.eclipse.starter.core.ThymeleafEngine;
import org.eclipse.starter.core.ZipCreator;
import org.eclipse.starter.core.model.JakartaSpecification;
import org.eclipse.starter.core.model.Project;
import org.thymeleaf.util.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class StarterService {

    @Inject
    private ThymeleafEngine thymeleafEngine;
    @Inject
    private ZipCreator zipCreator;

    private static String JAVA_PATH = "/src/main/java";
    private static String WEB_INF_PATH = "/src/main/webapp/WEB-INF";


    public byte[] generateArchive(
            String artifactId, String groupId, String packageName, String projectName, Map<String, String> specifications){

        Project project = populateModel(artifactId, groupId, packageName, projectName, specifications);

        Map<String, Object> variables = populateVariables(project);

        populateArchive(project, variables);

        return zipCreator.createArchive();

    }

    private void populateArchive(Project project, Map<String, Object> variables) {
        zipCreator.writeContents(project.getProjectName(), "pom.xml", thymeleafEngine.processFile("pom.xml", variables) );

        populateMandatoryFiles(project, variables);
    }

    private void populateMandatoryFiles(Project project, Map<String, Object> variables) {
        zipCreator.writeContents(
                project.getProjectName() + JAVA_PATH + getPackageAsDirectory(project.getPackageName()),
                StringUtils.capitalize(project.getProjectName()) + "RestApplication.java",
                thymeleafEngine.processFile("RestApplication.java", variables));
        zipCreator.writeContents(
                project.getProjectName() + JAVA_PATH + getPackageAsDirectory(project.getPackageName()),
                "HelloController.java",
                thymeleafEngine.processFile("HelloController.java", variables));
        zipCreator.writeContents(
                project.getProjectName()  + WEB_INF_PATH  , "web.xml", thymeleafEngine.processFile("web.xml", variables) );
    }

    private Map<String, Object> populateVariables(Project project) {
        Map<String, Object> variables = new HashMap<>();

        variables.put("artifactId", project.getArtifactId());
        variables.put("groupId", project.getGroupId());
        variables.put("dependencies", project.getJakartaSpecifications());
        variables.put("packageName", project.getPackageName());
        variables.put("projectName", project.getProjectName());

        return variables;
    }

    private Project populateModel(
            String artifactId, String groupId, String packageName, String projectName, Map<String, String> specifications) {

        Project project = new Project();

        if(artifactId  == null|| artifactId.trim().equals("")){
            artifactId = "myproject";
        }

        project.setArtifactId(artifactId);

        if(groupId == null || groupId.trim().equals("")){
            groupId = "com.myproject";
        }

        project.setGroupId(groupId);

        if(projectName == null || projectName.trim().equals("")){
            projectName = "MyProject";
        }

        project.setProjectName(projectName);

        if(packageName == null || packageName.trim().equals("")){
            packageName = "myproject";
        }

        project.setPackageName(packageName);

        for(Map.Entry<String, String> specification : specifications.entrySet() ){

            JakartaSpecOption jakartaSpecOption = JakartaSpecOption.getSpec(specification.getKey());

            JakartaSpecification jakartaSpecification  = new JakartaSpecification();

            jakartaSpecification.setArtifactId(jakartaSpecOption.getArtifactId());
            jakartaSpecification.setGroupId(jakartaSpecOption.getGroupId());
            jakartaSpecification.setName(jakartaSpecOption.getName());
            jakartaSpecification.setVersion(specification.getValue());

            project.addJakartaSpecification(jakartaSpecification);

        }

        addMandatoryDependency(project);

        return project;

    }

    private String getPackageAsDirectory(String packageName){
        return "/" + packageName.replace(".", "/");
    }

    private void addMandatoryDependency(Project project){

        List<JakartaSpecification> jakartaSpecifications = project.getJakartaSpecifications();

        Optional<JakartaSpecification> jakartaSpecificationOptional =
            jakartaSpecifications.stream(
        ).filter(jakartaSpecification -> jakartaSpecification.getName().equals(JakartaSpecOption.JAX_RS.getName())
        ).findAny();

        if(jakartaSpecificationOptional.isPresent()){
            return;
        }

        JakartaSpecOption jakartaSpecOption = JakartaSpecOption.JAX_RS;

        JakartaSpecification jakartaSpecification  = new JakartaSpecification();

        jakartaSpecification.setArtifactId(jakartaSpecOption.getArtifactId());
        jakartaSpecification.setGroupId(jakartaSpecOption.getGroupId());
        jakartaSpecification.setName(jakartaSpecOption.getName());
        jakartaSpecification.setVersion(jakartaSpecOption.getVersions()[0]);

        project.addJakartaSpecification(jakartaSpecification);


    }


}
