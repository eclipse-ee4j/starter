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
