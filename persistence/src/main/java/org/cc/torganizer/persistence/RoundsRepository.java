package org.cc.torganizer.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.PositionalOpponent;
import org.cc.torganizer.core.entities.Round;

@Stateless
public class RoundsRepository extends Repository {

  @Inject
  private DisciplinesRepository disciplineRepo;

  public RoundsRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  RoundsRepository(EntityManager entityManager, DisciplinesRepository disciplineRepo) {
    this.entityManager = entityManager;
    this.disciplineRepo = disciplineRepo;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Round CRUD
  //
  //-----------------------------------------------------------------------------------------------
  public Round create(Round round) {
    // client can send '0' with a detached object exception as the result
    round.setId(null);

    entityManager.persist(round);
    entityManager.flush();

    return round;
  }

  public Round read(Long roundId) {
    return entityManager.find(Round.class, roundId);
  }

  public List<Round> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Round> namedQuery = entityManager.createNamedQuery("Round.findAll", Round.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  public Round update(Round round) {
    return entityManager.merge(round);
  }

  public Round delete(Long roundId) {
    Round round = entityManager.find(Round.class, roundId);

    entityManager.remove(round);

    return round;
  }

  public Set<Opponent> getOpponents(Long roundId) {
    Long prevRoundId = getPrevRoundId(roundId);
    Set<Opponent> opponents;

    if (prevRoundId != null) {
      Round prevRound = read(prevRoundId);
      opponents = prevRound.getQualifiedOpponents();
    } else {
      Long disciplineId = disciplineRepo.getDisciplineId(roundId);
      opponents = new HashSet<>(disciplineRepo.getOpponents(disciplineId, 0, 999));
    }

    return opponents;
  }

  /**
   * Find all opponents, which are not already assigned to a group of the round.
   */
  public Set<Opponent> getOpponentsAssignableToGroup(Long roundId) {
    Set<Opponent> opponents = getOpponents(roundId);
    Set<Opponent> alreadyAssignedOpponents = getAlreadyAssignedOpponents(roundId);

    Set<Opponent> assignableOpponents = opponents;
    assignableOpponents.removeAll(alreadyAssignedOpponents);

    return assignableOpponents;
  }

  public Set<Opponent> getAlreadyAssignedOpponents(Long roundId) {
    TypedQuery<PositionalOpponent> query = entityManager.createQuery("SELECT po "
            + "FROM Round r, Group g , PositionalOpponent po "
            + "WHERE r.id = :roundId "
            + "AND g MEMBER OF r.groups "
            + "AND po MEMBER OF g.positionalOpponents",
        PositionalOpponent.class);
    query.setParameter("roundId", roundId);
    List<PositionalOpponent> resultList = query.getResultList();

    Set<Opponent> alreadyAssignedOpponents = new HashSet<>(resultList.size());
    for (PositionalOpponent po : resultList) {
      alreadyAssignedOpponents.add(po.getOpponent());
    }

    return alreadyAssignedOpponents;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Round groups
  //
  //-----------------------------------------------------------------------------------------------
  public List<Group> getGroups(Long roundId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Group> cq = cb.createQuery(Group.class);
    Root<Round> round = cq.from(Round.class);
    Root<Group> group = cq.from(Group.class);
    Join<Round, Group> roundGroupJoin = round.join("groups");

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

  public Round addGroup(Long roundId, Long groupId) {
    Group group = entityManager.find(Group.class, groupId);
    Round round = read(roundId);

    round.getGroups().add(group);
    entityManager.persist(round);

    return round;
  }

  public Round removeGroup(Long roundId, Long groupId) {
    Group group = entityManager.find(Group.class, groupId);
    Round round = read(roundId);

    round.getGroups().remove(group);
    entityManager.persist(round);

    return round;
  }

  public Long getRoundId(Long groupId) {
    Long roundId = null;

    try {
      TypedQuery<Long> query = entityManager.createQuery("SELECT r.id FROM Round r, Group g "
          + "WHERE g.id = :groupId "
          + "AND g MEMBER OF r.groups", Long.class);
      query.setParameter("groupId", groupId);
      roundId = query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }

    return roundId;
  }

  public Long getRoundId(Long disciplineId, Integer roundPosition) {
    Long roundId = null;

    try {
      TypedQuery<Long> query = entityManager.createQuery("SELECT r.id FROM Round r, Discipline d "
          + "WHERE d.id = :disciplineId "
          + "AND r MEMBER OF d.rounds "
          + "AND r.position = :roundPosition", Long.class);
      query.setParameter("disciplineId", disciplineId);
      query.setParameter("roundPosition", roundPosition);
      roundId = query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }

    return roundId;
  }

  public Long getPrevRoundId(Long roundId) {
    Integer roundPosition = getPosition(roundId);
    Integer prevRoundPosition = roundPosition - 1;
    Long disciplineId = disciplineRepo.getDisciplineId(roundId);
    return getRoundId(disciplineId, prevRoundPosition);
  }

  public Integer getPosition(Long roundId) {
    try {
      TypedQuery<Integer> query = entityManager.createQuery("SELECT r.position FROM Round r "
          + "WHERE r.id = :roundId", Integer.class);
      query.setParameter("roundId", roundId);
      return query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }
  }
}
