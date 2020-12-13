package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
public class PlayersRepository extends Repository<Player> {

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
  @Override
  public Player read(Long playerId) {
    return entityManager.find(Player.class, playerId);
  }

  @Override
  public List<Player> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findAll",
        Player.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public long count() {
    Query query = entityManager.createQuery("SELECT count(p) FROM Player p");
    return (long) query.getSingleResult();
  }
}
