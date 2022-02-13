package org.eclipse.starter.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import org.eclipse.starter.model.CafeRepository;
import org.eclipse.starter.model.entity.Coffee;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import fish.payara.arquillian.ws.rs.core.MediaType;

@RunWith(Arquillian.class)
public class CafeResourceTest {

	private static final String BASE_URI = "http://localhost:9090/jakarta-starter-test/rest/coffees";

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserTransaction transaction;

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "jakarta-starter-test.war").addClass(CafeResource.class)
				.addClass(CafeRepository.class).addClass(Coffee.class)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource("test-web.xml", "web.xml");
	}

	@Test
	public void testCreateCoffee() {
		Coffee coffee = null;

		TypedQuery<Coffee> query = entityManager.createQuery("SELECT o FROM Coffee o WHERE o.name = :name",
				Coffee.class);
		query.setParameter("name", "Test-1");

		try {
			coffee = query.getSingleResult();
			fail("No entity should have been found.");
		} catch (NoResultException ne) {
			// Expected
		}

		coffee = new Coffee("Test-1", 7.25);
		ClientBuilder.newClient().target(BASE_URI).request(MediaType.APPLICATION_JSON).post(Entity.json(coffee));

		query = entityManager.createQuery("SELECT o FROM Coffee o WHERE o.name = :name", Coffee.class);
		query.setParameter("name", "Test-1");

		coffee = query.getSingleResult();

		assertNotNull(coffee);
		assertEquals(coffee.getName(), "Test-1");
		assertEquals(coffee.getPrice().doubleValue(), 7.25, 0);
	}

	@Test
	public void testGetCoffeeById() throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		transaction.begin();
		Coffee coffee = new Coffee("Test-2", 5.99);
		entityManager.persist(coffee);
		transaction.commit();

		coffee = ClientBuilder.newClient().target(BASE_URI).path(coffee.getId().toString())
				.request(MediaType.APPLICATION_JSON).get(Coffee.class);

		assertNotNull(coffee);
		assertEquals(coffee.getName(), "Test-2");
		assertEquals(coffee.getPrice().doubleValue(), 5.99, 0);
	}

	@After
	public void deleteCoffees() throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
			RollbackException, HeuristicMixedException, HeuristicRollbackException {
		transaction.begin();
		entityManager.createQuery("DELETE FROM Coffee").executeUpdate();
		transaction.commit();
	}
}