package org.eclipse.starter.rest;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.starter.model.CafeRepository;
import org.eclipse.starter.model.entity.Coffee;

@Path("coffees")
public class CafeResource {

	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

	@Inject
	private CafeRepository cafeRepository;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<Coffee> getAllCoffees() {
		return this.cafeRepository.getAllCoffees();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Coffee createCoffee(Coffee coffee) {
		try {
			return this.cafeRepository.persistCoffee(coffee);
		} catch (PersistenceException e) {
			logger.log(Level.SEVERE, "Error creating coffee {0}: {1}.", new Object[] { coffee, e });
			throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Coffee getCoffeeById(@PathParam("id") Long coffeeId) {
		return this.cafeRepository.findCoffeeById(coffeeId);
	}

	@DELETE
	@Path("{id}")
	public void deleteCoffee(@PathParam("id") Long coffeeId) {
		try {
			this.cafeRepository.removeCoffeeById(coffeeId);
		} catch (IllegalArgumentException ex) {
			logger.log(Level.SEVERE, "Error calling deleteCoffee() for coffeeId {0}: {1}.",
					new Object[] { coffeeId, ex });
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}
}