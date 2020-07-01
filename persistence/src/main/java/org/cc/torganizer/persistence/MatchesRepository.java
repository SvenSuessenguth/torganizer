package org.cc.torganizer.persistence;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.entities.Match;

@RequestScoped
public class MatchesRepository extends Repository<Match> {

  public MatchesRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  MatchesRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Match read(Long matchId) {
    return entityManager.find(Match.class, matchId);
  }

  @Override
  public List<Match> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Match> namedQuery = entityManager.createNamedQuery("Match.findAll", Match.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public long count() {
    Query query = entityManager.createQuery("SELECT count(m) FROM Match m");
    return (long) query.getSingleResult();
  }
}
