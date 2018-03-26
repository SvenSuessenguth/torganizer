package org.cc.torganizer.persistence;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DisciplinesRepository extends Repository{

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  public DisciplinesRepository() {
  }

  /**
   * Constructor for testing.
   */
  public DisciplinesRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
