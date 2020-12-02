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
 *   Jeyvison Nascimento - Create JSONBHandler
 ********************************************************************************/

package org.eclipse.starter.core.specification.handler;

import org.eclipse.starter.core.StringPool;
import org.eclipse.starter.core.ThymeleafEngine;
import org.eclipse.starter.core.ZipCreator;
import org.eclipse.starter.core.model.Project;

import java.util.Map;

public class JSONBHandler implements SpecificationHandler {

    private static String JSONB_PACKAGE = "/jsonb";

    @Override
    public void handle(Project project, ThymeleafEngine thymeleafEngine, Map<String, Object> variables, ZipCreator zipCreator) {

        StringBuilder sb = new StringBuilder(50);

        String javaDirectory =
                sb.append(
                        project.getProjectName()
                ).append(
                        StringPool.JAVA_PATH
                ).append(
                        getPackageAsDirectory(project.getPackageName())
                ).append(
                        JSONB_PACKAGE
                ).toString();

        zipCreator.writeContents(
                javaDirectory,
                "Employee.java",
                thymeleafEngine.processFile("json-b/Employee.java.tpl", variables));
        zipCreator.writeContents(
                javaDirectory,
                "EmployeeService.java",
                thymeleafEngine.processFile("json-b/EmployeeService.java.tpl", variables));


    }
}
