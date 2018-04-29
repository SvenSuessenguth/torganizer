package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
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
  // Round groups
  //
  //--------------------------------------------------------------------------------------------------------------------
  public List<Group> getGroups(Long roundId, Integer offset, Integer maxResults){
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults==null?DEFAULT_MAX_RESULTS:maxResults;

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Group> cq = cb.createQuery(Group.class);
    Root<Round> round = cq.from(Round.class);
    Root<Group> group = cq.from(Group.class);
    Join<Round, Group> roundGroupJoin = round.join ("groups");

    cq.select(group);
    cq.where(
      cb.and(
        cb.equal(round.get("id"), roundId),
        cb.equal(roundGroupJoin.get("id"), group.get("id"))
      )
    );

    TypedQuery<Group> query = entityManager.createQuery(cq);
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();

  }

  public Round addOpponent(Long roundId, Long opponentId){
    return null;
  }

  public Round removeOpponent(Long roundId, Long opponentId) {
    return null;
  }
}
