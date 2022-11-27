package org.cc.torganizer.persistence;

import static jakarta.transaction.Transactional.TxType.NEVER;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;

/**
 * Accessing the Repository for Clubs and related entities.
 */
@RequestScoped
public class ClubsRepository extends Repository<Club> {

  @SuppressWarnings("unused")
  public ClubsRepository() {
  }

  /**
   * Constructor for testing purpose.
   *
   * @param entityManager EntityManager
   */
  ClubsRepository(EntityManager entityManager) {
    this.em = entityManager;
  }


  //-----------------------------------------------------------------------------------------------
  //
  // Tournaments CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  @Transactional(NEVER)
  public Club read(Long clubId) {
    return em.find(Club.class, clubId);
  }

  @Override
  @Transactional(NEVER)
  public List<Club> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var namedQuery = em.createNamedQuery("Club.findAll", Club.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  @Transactional(NEVER)
  public long count() {
    var query = em.createQuery("SELECT count(c) FROM Club c");
    return (long) query.getSingleResult();
  }

  /**
   * reading all players from athe given club.
   */
  @Transactional(NEVER)
  public List<Player> getPlayers(Club club) {
    var namedQuery = em.createNamedQuery("Club.findPlayers",
        Player.class);
    namedQuery.setParameter("club", club);
    return namedQuery.getResultList();
  }

  /**
   * count the players from the given club.
   */
  @Transactional(NEVER)
  public Long countPlayers(Club club) {
    var namedQuery = em.createNamedQuery("Club.countPlayers", Long.class);
    namedQuery.setParameter("club", club);

    return namedQuery.getSingleResult();
  }
}
