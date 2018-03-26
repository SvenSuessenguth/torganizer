package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Discipline;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Tournaments CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Discipline getDiscipline(Long disciplineId){
    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery("Discipline.findById", Discipline.class);
    namedQuery.setParameter("id", disciplineId);

    return namedQuery.getSingleResult();
  }
}
