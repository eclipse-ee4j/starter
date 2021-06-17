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
 *   Jeyvison Nascimento - Create Specification Handler
 ********************************************************************************/

package org.eclipse.starter.core.specification.handler;

import org.eclipse.starter.core.ThymeleafEngine;
import org.eclipse.starter.core.ZipCreator;
import org.eclipse.starter.core.model.Project;

import java.util.Map;

public interface SpecificationHandler {

    public void handle(Project project, ThymeleafEngine thymeleafEngine, Map<String, Object> variables, ZipCreator zipCreator);

    public default String getCoordinateAsDirectory(String packageName) {
        return packageName.replace(".", "/");
    }
}
