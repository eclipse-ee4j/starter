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

package org.eclipse.starter.api.endpoint;


import org.eclipse.starter.api.dto.ProjectDTO;
import org.eclipse.starter.core.service.StarterService;
import org.thymeleaf.util.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Objects;

@Path("v1")
public class APIEndpointV1 {

    @Inject
    private StarterService starterService;

    @POST
    @Produces("application/zip")
    public Response createArchive(ProjectDTO projectDTO) {

        if (projectDTO.getJakartaVersion() ==  null ||
                (!StringUtils.contains(projectDTO.getJakartaVersion(), "8.0.0") &&
                !StringUtils.contains(projectDTO.getJakartaVersion(), "9.1.0"))) {

            return Response.status(400).entity("jakarta version must be 8.0.0 or 9.1.0").build();
        }

        byte[] archive = starterService.generateArchive(
                projectDTO.getArtifactId(),
                projectDTO.getGroupId(),
                projectDTO.getProjectName(),
                projectDTO.getJakartaVersion()
        );

        return Response
                .ok()
                .header("Content-Length", archive.length)
                .header("Content-Disposition", "attachment; filename=\"" + "project" + "\"")
                .type("application/zip")
                .entity(archive)
                .build();

    }


}
