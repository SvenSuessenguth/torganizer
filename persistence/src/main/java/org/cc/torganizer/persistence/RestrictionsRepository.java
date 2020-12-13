package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.cc.torganizer.core.entities.Restriction;

/**
 * Accessing the Repository for Restrictions and related entities.
 */
@RequestScoped
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
