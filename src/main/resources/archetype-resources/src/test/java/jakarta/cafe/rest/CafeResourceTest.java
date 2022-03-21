package ${package}.jakarta.cafe.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import ${package}.jakarta.cafe.model.CafeRepository;
import ${package}.jakarta.cafe.model.entity.Coffee;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CafeResourceTest {

	private static final String BASE_URI = "http://localhost:9090/jakartaee-cafe-test/rest/coffees";

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserTransaction transaction;

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class, "jakartaee-cafe-test.war").addClass(CafeResource.class)
				.addClass(CafeRepository.class).addClass(Coffee.class)
				.addAsResource("test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource("test-web.xml", "web.xml");
	}

	@Test
	public void testCreateCoffee() {
		Coffee coffee = null;

		TypedQuery<Coffee> query = entityManager.createQuery("SELECT o FROM Coffee o WHERE o.name = :name",
				Coffee.class);
		query.setParameter("name", "Test-A");

		try {
			coffee = query.getSingleResult();
			fail("No entity should have been found.");
		} catch (NoResultException ne) {
			// Expected
		}

		coffee = new Coffee("Test-A", 7.25);
		ClientBuilder.newClient().target(BASE_URI).request(MediaType.APPLICATION_JSON).post(Entity.json(coffee));

		query = entityManager.createQuery("SELECT o FROM Coffee o WHERE o.name = :name", Coffee.class);
		query.setParameter("name", "Test-A");

		coffee = query.getSingleResult();

		assertNotNull(coffee);
		assertEquals(coffee.getName(), "Test-A");
		assertEquals(coffee.getPrice().doubleValue(), 7.25, 0);
	}

	@Test
	public void testGetCoffeeById() throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		transaction.begin();
		Coffee coffee = new Coffee("Test-B", 5.99);
		entityManager.persist(coffee);
		transaction.commit();

		coffee = ClientBuilder.newClient().target(BASE_URI).path(coffee.getId().toString())
				.request(MediaType.APPLICATION_JSON).get(Coffee.class);

		assertNotNull(coffee);
		assertEquals(coffee.getName(), "Test-B");
		assertEquals(coffee.getPrice().doubleValue(), 5.99, 0);
	}

	@Test
	public void testGetAllCoffees() throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		transaction.begin();
		Coffee coffee = new Coffee("Test-C", 4.75);
		entityManager.persist(coffee);
		coffee = new Coffee("Test-D", 1.99);
		entityManager.persist(coffee);
		coffee = new Coffee("Test-E", 2.95);
		entityManager.persist(coffee);
		transaction.commit();

		List<Coffee> coffees = ClientBuilder.newClient().target(BASE_URI).request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Coffee>>() {
				});

		assertEquals(coffees.size(), 3);
		assertEquals(coffees.get(0).getName(), "Test-C");
		assertEquals(coffees.get(0).getPrice().doubleValue(), 4.75, 0);
		assertEquals(coffees.get(1).getName(), "Test-D");
		assertEquals(coffees.get(1).getPrice().doubleValue(), 1.99, 0);
		assertEquals(coffees.get(2).getName(), "Test-E");
		assertEquals(coffees.get(2).getPrice().doubleValue(), 2.95, 0);
	}

	@Test
	public void testDeleteCoffee() throws NotSupportedException, SystemException, SecurityException,
			IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
		transaction.begin();
		Coffee coffee = new Coffee("Test-Z", 7.77);
		entityManager.persist(coffee);
		transaction.commit();

		ClientBuilder.newClient().target(BASE_URI).path(coffee.getId().toString()).request().delete();

		TypedQuery<Coffee> query = entityManager.createQuery("SELECT o FROM Coffee o WHERE o.name = :name",
				Coffee.class);
		query.setParameter("name", "Test-Z");

		try {
			coffee = query.getSingleResult();
			fail("No entity should have been found.");
		} catch (NoResultException ne) {
			// Expected
		}
	}

	@After
	public void deleteCoffees() throws NotSupportedException, SystemException, SecurityException, IllegalStateException,
			RollbackException, HeuristicMixedException, HeuristicRollbackException {
		transaction.begin();
		entityManager.createQuery("DELETE FROM Coffee").executeUpdate();
		transaction.commit();
	}
}