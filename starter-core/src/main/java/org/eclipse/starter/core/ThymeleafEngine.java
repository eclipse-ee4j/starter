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
 *      Initially authored in Atbash Jessie
 ********************************************************************************/

package org.eclipse.starter.core;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.StringWriter;
import java.util.Map;

@ApplicationScoped
public class ThymeleafEngine {

    private TemplateEngine engine;

    @PostConstruct
    public void init() {
        AbstractConfigurableTemplateResolver resolver = new FileTemplateResolver();
        resolver.setTemplateMode(TemplateMode.TEXT);
        engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
    }

    public String processFile(String file, Map<String, Object> variables) {
        StringWriter writer = new StringWriter();
        Context context = new Context();

        for (Map.Entry<String, Object> variable : variables.entrySet()) {
            context.setVariable(variable.getKey(), variable.getValue());
        }

        engine.process(file, context, writer);

        return writer.toString();

    }

}
