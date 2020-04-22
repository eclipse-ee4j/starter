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
import org.eclipse.starter.core.*;
import org.eclipse.starter.core.service.StarterService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("v1")
public class APIEndpointV1 {

    @Inject
    private StarterService starterService;


    @GET
    @Path("/specifications")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableSpecs() {
        return Response.ok().entity(JakartaSpecOption.values()).build();

    }


    @POST
    @Produces("application/zip")
    public Response createArchive(ProjectDTO projectDTO) {

        Response response = validateProject(projectDTO);

        if (response != null) {
            return response;
        }

        byte[] archive = starterService.generateArchive(
                projectDTO.getArtifactId(),
                projectDTO.getGroupId(),
                projectDTO.getPackageName(),
                projectDTO.getProjectName(),
                projectDTO.getSpecifications()
        );

        return Response
                .ok()
                .header("Content-Length", archive.length)
                .header("Content-Disposition", "attachment; filename=\"" + "test" + "\"")
                .type("application/zip")
                .entity(archive)
                .build();

    }


    private Response validateProject(ProjectDTO projectDTO) {

        Map<String, String> specifications = projectDTO.getSpecifications();

        if (specifications == null || specifications.isEmpty()) {
            return Response.status(
                    Response.Status.BAD_REQUEST
            ).entity(
                    "No specifications informed"
            ).build();

        }

        for (Map.Entry<String, String> specification : projectDTO.getSpecifications().entrySet()) {

            JakartaSpecOption jakartaSpecOption = JakartaSpecOption.getSpec(specification.getKey());

            if (jakartaSpecOption == null) {
                return Response.status(
                        Response.Status.NOT_FOUND
                ).entity(
                        "No specification found with name: " + specification.getKey()
                ).build();
            }

            for (String version : jakartaSpecOption.getVersions()) {
                if (version.equals(specification.getValue())) {
                    return null;
                }
            }

            return Response.status(
                    Response.Status.NOT_FOUND
            ).entity(
                    "No version found: " + specification.getValue() + " for specification " + specification.getKey()
            ).build();
        }

        return null;

    }


}
