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
 *   Jeyvison Nascimento - Create JSONPHandler
 ********************************************************************************/

package org.eclipse.starter.core.template.generator;

import org.eclipse.starter.core.StringPool;
import org.eclipse.starter.core.ThymeleafEngine;
import org.eclipse.starter.core.ZipCreator;
import org.eclipse.starter.core.model.Project;

import java.util.Map;

public class JSONPTemplateGenerator implements TemplateGenerator {

    private static String JSONP_PACKAGE = "/jsonp";

    @Override
    public void generate(Project project, ThymeleafEngine thymeleafEngine, Map<String, Object> variables, ZipCreator zipCreator) {

        StringBuilder sb = new StringBuilder(50);

        String javaDirectory =
                sb.append(
                        project.getProjectName()
                ).append(
                        StringPool.JAVA_PATH
                ).append(
                        getCoordinateAsDirectory(project.getPackageName())
                ).append(
                        JSONP_PACKAGE
                ).toString();

        zipCreator.writeContents(
                javaDirectory,
                "EmployeeService.java",
                thymeleafEngine.processFile("json-p/EmployeeService.java.tpl", variables));

    }
}
