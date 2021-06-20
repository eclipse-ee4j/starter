/********************************************************************************
 * Copyright (c) 2020 Hantsy Bai and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Hantsy Bai - initial version.
 ********************************************************************************/
package $package;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.ok;

@Path("greeting")
@RequestScoped
public class GreetingResource {

    @Inject
    private GreetingService greetingService;

    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response greeting(@PathParam("name") String name) {
        return ok(this.greetingService.buildGreetingMessage(name)).build();
    }
}
