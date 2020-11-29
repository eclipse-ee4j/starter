package org.eclipse.starter.core.specification.handler;

import org.eclipse.starter.core.ThymeleafEngine;
import org.eclipse.starter.core.ZipCreator;
import org.eclipse.starter.core.model.Project;

import java.util.Map;

public interface SpecificationHandler {

    public void handle(Project project, ThymeleafEngine thymeleafEngine, Map<String, Object> variables, ZipCreator zipCreator);
}
