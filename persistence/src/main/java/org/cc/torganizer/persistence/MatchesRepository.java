package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Tournament;

/**
 * Accessing the Repository for Matches and related entities.
 */
@RequestScoped
public class MatchesRepository extends Repository<Match> {

  @SuppressWarnings("unused")
  public MatchesRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  MatchesRepository(EntityManager entityManager) {
    this.em = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Match read(Long matchId) {
    return em.find(Match.class, matchId);
  }

  @Override
  public List<Match> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var namedQuery = em.createNamedQuery("Match.findAll", Match.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  public long count() {
    var query = em.createQuery("SELECT count(m) FROM Match m");
    return (long) query.getSingleResult();
  }

  /**
   * Reading all running matches.
   */
  public List<Match> getRunningMatches(Tournament tournament) {
    var namedQuery = em.createNamedQuery("Match.runningMatches", Match.class);
    namedQuery.setParameter("tournament", tournament.getId());

    return namedQuery.getResultList();
  }

  /**
   * Reading all finished matches.
   */
  public List<Match> getFinishedMatches(Tournament tournament) {
    var namedQuery = em.createNamedQuery("Match.finishedMatches", Match.class);
    namedQuery.setParameter("tournament", tournament.getId());

    return namedQuery.getResultList();
  }
}
