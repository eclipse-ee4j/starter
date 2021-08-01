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
 *   Jeyvison Nascimento - Create JAXRSHandler
 ********************************************************************************/

package org.eclipse.starter.core.template.generator;

import org.eclipse.starter.core.StringPool;
import org.eclipse.starter.core.ThymeleafEngine;
import org.eclipse.starter.core.ZipCreator;
import org.eclipse.starter.core.model.Project;
import org.thymeleaf.util.StringUtils;

import java.util.Map;

public class JAXRSTemplateGenerator implements TemplateGenerator {

    private static String WEB_INF_PATH = "/src/main/webapp/WEB-INF";
    private static String JAXRS_PACKAGE = "/jaxrs";


    public void generate(Project project, ThymeleafEngine thymeleafEngine, Map<String, Object> variables, ZipCreator zipCreator) {

        StringBuilder sb = new StringBuilder(50);

        String javaDirectory =
                sb.append(
                        project.getArtifactId()
                ).append(
                        StringPool.JAVA_PATH
                ).append(
                        getCoordinateAsDirectory(project.getPackageName())
                ).append(
                        JAXRS_PACKAGE
                ).toString();

        zipCreator.writeContents(
                javaDirectory,
                "HelloController.java",
                thymeleafEngine.processFile("jax-rs/HelloController.java.tpl", variables));
        zipCreator.writeContents(
                javaDirectory, StringUtils.capitalize(project.getProjectName()) + "RestApplication.java",
                thymeleafEngine.processFile("jax-rs/RestApplication.java.tpl", variables));
        zipCreator.writeContents(
                project.getProjectName() + WEB_INF_PATH, "web.xml", thymeleafEngine.processFile("jax-rs/web.xml.tpl", variables));

    }

}
