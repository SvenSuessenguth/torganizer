package org.cc.torganizer.persistence;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.entities.Club;

@Stateless
public class ClubsRepository extends Repository<Club> {

  public ClubsRepository() {
  }

  /**
   * Constructor for testing purpose.
   *
   * @param entityManager EntityManager
   */
  ClubsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Club read(Long clubId) {
    return entityManager.find(Club.class, clubId);
  }

  @Override
  public List<Club> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findAll", Club.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }
}
