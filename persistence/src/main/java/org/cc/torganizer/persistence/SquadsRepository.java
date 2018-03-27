package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class SquadsRepository extends Repository{

  public SquadsRepository() {
  }

  /**
   * Constructor for testing.
   */
  public SquadsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Squad create(Squad squad){
    entityManager.persist(squad);
    entityManager.flush();

    return squad;
  }
  public Squad read(Long squadId){
    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findById", Squad.class);
    namedQuery.setParameter("id", squadId);

    return namedQuery.getSingleResult();
  }

  public List<Squad> read(Integer offset, Integer maxResults){
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findAll", Squad.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }
}
