package org.cc.torganizer.persistence;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

@RequestScoped
public class SquadsRepository extends Repository<Squad> {

  public SquadsRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  SquadsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Squad read(Long squadId) {
    return entityManager.find(Squad.class, squadId);
  }

  @Override
  public List<Squad> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findAll", Squad.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  /**
   * Reading squads and sort them by the last name of the players.
   */
  public List<Squad> readOrderByLastName(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findAll", Squad.class);
    List<Squad> squads = namedQuery.getResultList();

    Collections.sort(squads, new OpponentByNameComparator());
    return squads.subList(offset, offset + maxResults);
  }

  public long count() {
    Query query = entityManager.createQuery("SELECT count(s) FROM Squad s");
    return (long) query.getSingleResult();
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Squads players
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * adding a player to an already persisted squad.
   */
  public Player addPlayer(Long squadId, Long playerId) {
    Player player = entityManager.find(Player.class, playerId);
    Squad squad = entityManager.find(Squad.class, squadId);

    Collection<Player> players = squad.getPlayers();
    players.add(player);
    entityManager.persist(squad);
    // to get the id
    entityManager.flush();

    return player;
  }

  /**
   * Getting all players which are related to the squad with the given id.
   */
  public List<Player> getPlayers(Long squadId) {
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Squad.findPlayers",
        Player.class);
    namedQuery.setParameter("id", squadId);

    return namedQuery.getResultList();
  }
}
