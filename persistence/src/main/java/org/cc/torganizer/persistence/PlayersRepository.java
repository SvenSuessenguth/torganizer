package org.cc.torganizer.persistence;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.cc.torganizer.core.entities.Player;

@Stateless
public class PlayersRepository extends Repository {

  public PlayersRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  PlayersRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  public Player create(Player player) {
    entityManager.persist(player);
    // with no flush, the id is unknown
    entityManager.flush();

    return player;
  }

  public Player read(Long playerId) {
    return entityManager.find(Player.class, playerId);
  }

  public List<Player> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findAll",
        Player.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public Player update(Player player) {
    entityManager.merge(player);

    return player;
  }

  public Player delete(Long playerId) {
    Player player = entityManager.find(Player.class, playerId);

    entityManager.remove(player);

    return player;
  }

  public long count() {
    Query query = entityManager.createQuery("SELECT count(p) FROM Player p");
    return (long) query.getSingleResult();
  }
}
