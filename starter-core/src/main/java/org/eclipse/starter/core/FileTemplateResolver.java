package org.eclipse.starter.core;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresource.ClassLoaderTemplateResource;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

public class FileTemplateResolver extends AbstractConfigurableTemplateResolver {
    @Override
    protected ITemplateResource computeTemplateResource(
            IEngineConfiguration configuration, String ownerTemplate,
            String template, String resourceName, String characterEncoding,
            Map<String, Object> templateResolutionAttributes) {


        return new ClassLoaderTemplateResource(
                FileTemplateResolver.class.getClassLoader(), resourceName, characterEncoding);
    }
}
