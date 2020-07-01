package org.cc.torganizer.persistence;

import static javax.transaction.Transactional.TxType.NEVER;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
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
  @Transactional(NEVER)
  public Club read(Long clubId) {
    return entityManager.find(Club.class, clubId);
  }

  @Override
  @Transactional(NEVER)
  public List<Club> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findAll", Club.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  @Transactional(NEVER)
  public long count() {
    Query query = entityManager.createQuery("SELECT count(c) FROM Club c");
    return (long) query.getSingleResult();
  }

  /**
   * reading all players from athe given club.
   */
  @Transactional(NEVER)
  public List<Player> getPlayers(Club club) {
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Club.findPlayers",
        Player.class);
    namedQuery.setParameter("club", club);
    return namedQuery.getResultList();
  }

  /**
   * count the players from the given club.
   */
  @Transactional(NEVER)
  public Long countPlayers(Club club) {
    TypedQuery<Long> namedQuery = entityManager.createNamedQuery("Club.countPlayers", Long.class);
    namedQuery.setParameter("club", club);

    return namedQuery.getSingleResult();
  }
}
