package org.cc.torganizer.persistence;

import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Stateless
public class SquadsRepository extends Repository {

  public SquadsRepository() {
  }

  /**
   * Constructor for testing.
   */
  public SquadsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Squad create(Squad squad) {
    entityManager.persist(squad);
    entityManager.flush();

    return squad;
  }

  public Squad read(Long squadId) {
    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findById", Squad.class);
    namedQuery.setParameter("id", squadId);

    return namedQuery.getSingleResult();
  }

  public List<Squad> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findAll", Squad.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  public List<Squad> readOrderByLastName(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findAll", Squad.class);
    List<Squad> squads = namedQuery.getResultList();

    Collections.sort(squads, new OpponentByNameComparator());
    return squads.subList(offset, offset + maxResults);
  }

  public Squad update(Squad squad) {
    entityManager. merge(squad);
    return squad;
  }

  public Squad delete(Long squadId) {
    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findById", Squad.class);
    namedQuery.setParameter("id", squadId);
    Squad squad = namedQuery.getSingleResult();

    entityManager.remove(squad);

    return squad;
  }

  public long count() {
    Query query = entityManager.createQuery("SELECT count(s) FROM Squad s");
    return (long) query.getSingleResult();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Squads players
  //
  //--------------------------------------------------------------------------------------------------------------------
  public List<Player> getPlayers(Long squadId) {
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Squad.findPlayers", Player.class);
    namedQuery.setParameter("id", squadId);

    return namedQuery.getResultList();
  }
}
