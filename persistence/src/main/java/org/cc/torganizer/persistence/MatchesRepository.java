package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Tournament;

/**
 * Accessing the Repository for Matches and related entities.
 */
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
    var query = entityManager.createQuery("SELECT count(m) FROM Match m");
    return (long) query.getSingleResult();
  }

  public List<Match> getRunningMatches(Tournament tournament) {
    TypedQuery<Match> namedQuery = entityManager.createNamedQuery("Match.runningMatches", Match.class);
    namedQuery.setParameter("tournament", tournament.getId());

    return namedQuery.getResultList();
  }

  public List<Match> getFinishedMatches(Tournament tournament) {
    TypedQuery<Match> namedQuery = entityManager.createNamedQuery("Match.finishedMatches", Match.class);
    namedQuery.setParameter("tournament", tournament.getId());

    return namedQuery.getResultList();
  }
}
