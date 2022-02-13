package org.eclipse.starter.rest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.eclipse.starter.model.CafeRepository;
import org.eclipse.starter.model.entity.Coffee;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Application layer integration test covering a number of otherwise fairly trivial components that
 * largely do not warrant their own tests.
 *
 * <p>Ensure a Payara instance is running locally before this test is executed, with the default
 * user name and password.
 */
@RunWith(Arquillian.class)
public class CafeResourceTest {

  @PersistenceContext private EntityManager entityManager;

  @Deployment
  public static WebArchive createDeployment() {
    return ShrinkWrap.create(WebArchive.class, "jakarta-starter-test.war")
        .addClass(CafeResource.class)
        .addClass(CafeRepository.class)
        .addClass(Coffee.class)
        .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
        .addAsResource("META-INF/initital-data.sql", "META-INF/initital-data.sql")        
        // Web application descriptor
        .addAsWebInfResource("test-web.xml", "web.xml");
  }

  @Test
  public void testGetAllCoffees() {
  }
}
