package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.cc.torganizer.core.entities.Player;

/**
 * Accessing the Repository for Players and related entities.
 */
@RequestScoped
public class PlayersRepository extends Repository<Player> {

  @SuppressWarnings("unused")
  public PlayersRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  PlayersRepository(EntityManager entityManager) {
    this.em = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Player read(Long playerId) {
    return em.find(Player.class, playerId);
  }

  @Override
  public List<Player> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Player> namedQuery = em.createNamedQuery("Player.findAll",
        Player.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public long count() {
    var query = em.createQuery("SELECT count(p) FROM Player p");
    return (long) query.getSingleResult();
  }
}
