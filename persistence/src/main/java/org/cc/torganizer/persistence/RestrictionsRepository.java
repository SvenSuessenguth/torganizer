package org.cc.torganizer.persistence;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.entities.Restriction;

@Stateless
public class RestrictionsRepository extends Repository<Restriction> {

  public RestrictionsRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  RestrictionsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Restriction read(Long restrictionId) {
    return entityManager.find(Restriction.class, restrictionId);
  }

  @Override
  public List<Restriction> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findAll",
        Restriction.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }
}
