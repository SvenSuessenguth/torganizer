package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Player;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PlayersRepository extends Repository{

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  public PlayersRepository() {
  }

  /**
   * Constructor for testing.
   */
  public PlayersRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public Player getPlayer(Long playerId) {
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
      namedQuery.setParameter("id",playerId);

    return namedQuery.getSingleResult();
  }

  public List<Player> getPlayers(){
    return entityManager.createNamedQuery("Player.findAll", Player.class).getResultList();
  }
}
