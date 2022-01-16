package cafe.model;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import cafe.model.entity.Coffee;

@Stateless
public class CafeRepository {

  private static final Logger logger =
      Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

  @PersistenceContext private EntityManager entityManager;

  public List<Coffee> getAllCoffees() {
    logger.log(Level.INFO, "Finding all coffees.");

    return this.entityManager.createNamedQuery("findAllCoffees", Coffee.class).getResultList();
  }

  public Coffee persistCoffee(Coffee coffee) {
    logger.log(Level.INFO, "Persisting the new coffee {0}.", coffee);
    this.entityManager.persist(coffee);
    return coffee;
  }

  public void removeCoffeeById(Long coffeeId) {
    logger.log(Level.INFO, "Removing a coffee {0}.", coffeeId);
    Coffee coffee = entityManager.find(Coffee.class, coffeeId);
    this.entityManager.remove(coffee);
  }

  public Coffee findCoffeeById(Long coffeeId) {
    logger.log(Level.INFO, "Finding the coffee with id {0}.", coffeeId);
    return this.entityManager.find(Coffee.class, coffeeId);
  }
}
