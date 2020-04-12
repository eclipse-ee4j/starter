package org.eclipse.starter.core;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.StringWriter;
import java.util.Map;

@ApplicationScoped
public class ThymeleafEngine {

    @Inject
    private FilesLocator filesLocator;


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

        String fileIndication = filesLocator.findFile(file);

        if (fileIndication == null) {
            throw new RuntimeException("File not found");
        }

        engine.process(fileIndication, context, writer);

        return writer.toString();

    }

}
