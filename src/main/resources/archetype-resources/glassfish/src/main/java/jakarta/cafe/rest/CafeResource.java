package ${package}.jakarta.cafe.rest;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ${jeePackage}.ejb.EJB;
import ${jeePackage}.persistence.PersistenceException;
import ${jeePackage}.ws.rs.Consumes;
import ${jeePackage}.ws.rs.DELETE;
import ${jeePackage}.ws.rs.GET;
import ${jeePackage}.ws.rs.POST;
import ${jeePackage}.ws.rs.Path;
import ${jeePackage}.ws.rs.PathParam;
import ${jeePackage}.ws.rs.Produces;
import ${jeePackage}.ws.rs.WebApplicationException;
import ${jeePackage}.ws.rs.core.MediaType;
import ${jeePackage}.ws.rs.core.Response;

import ${package}.jakarta.cafe.model.CafeRepository;
import ${package}.jakarta.cafe.model.entity.Coffee;

@Path("coffees")
public class CafeResource {

	private static final Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

	@EJB
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
