package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Restriction;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RestrictionsRepository extends Repository{

  public RestrictionsRepository() {
  }

  /**
   * Constructor for testing.
   */
  public RestrictionsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Restriction create(Restriction restriction)
  {
    entityManager.persist(restriction);
    entityManager.flush();

    return restriction;
  }

  public Restriction read(Long restrictionId) {
    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findById", Restriction.class);
    namedQuery.setParameter("id", restrictionId);

    return namedQuery.getSingleResult();
  }

  public List<Restriction> read(Integer offset, Integer maxResults){
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findAll", Restriction.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }
}
