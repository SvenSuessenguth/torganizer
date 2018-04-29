package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Round;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RoundsRepository extends Repository{

  public RoundsRepository() {
  }

  /**
   * Constructor for testing.
   */
  public RoundsRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Discipline CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Round create(Round round){
    // client can send '0' with a detached object exception as the result
    round.setId(null);

    entityManager.persist(round);
    entityManager.flush();

    return round;
  }

  public Round read(Long roundId){
    return entityManager.find(Round.class, roundId);
  }

  public List<Round> read(Integer offset, Integer maxResults){
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Round> namedQuery = entityManager.createNamedQuery("Round.findAll", Round.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  public Round update(Round round){
    return entityManager.merge(round);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Discipline opponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  public List<Opponent> getOpponents(Long roundId, Integer offset, Integer maxResults){
    return null;
  }

  public Round addOpponent(Long roundId, Long opponentId){
    return null;
  }

  public Round removeOpponent(Long roundId, Long opponentId) {
    return null;
  }
}
