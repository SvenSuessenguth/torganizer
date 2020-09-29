package org.cc.torganizer.persistence;

import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.PositionalOpponent;
import org.cc.torganizer.core.entities.Round;

@RequestScoped
public class RoundsRepository extends Repository<Round> {

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
  @Override
  public Round read(Long roundId) {
    return entityManager.find(Round.class, roundId);
  }

  @Override
  public List<Round> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<Round> namedQuery = entityManager.createNamedQuery("Round.findAll", Round.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  /**
   * Reading the opponents which are related to the round with the given id.
   */
  public Collection<Opponent> getOpponents(Long roundId) {
    Long prevRoundId = getPrevRoundId(roundId);
    Collection<Opponent> opponents;

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
  public Collection<Opponent> getNotAssignedOpponents(Long roundId, Integer offset, Integer maxResults) {

    Collection<Opponent> opponents = getOpponents(roundId);
    Set<Opponent> assignedOpponents = getAssignedOpponents(roundId);

    // sets can't work with positions
    List<Opponent> notAssignedOpponents = new ArrayList<>(opponents);
    notAssignedOpponents.removeAll(assignedOpponents);
    sort(notAssignedOpponents, new OpponentByNameComparator());

    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;
    Integer foundOpponentsSize = notAssignedOpponents.size();
    maxResults = maxResults > foundOpponentsSize ? foundOpponentsSize : maxResults;

    return new HashSet<>(notAssignedOpponents.subList(offset, maxResults));
  }

  /**
   * Getting all opponents which are assigned to the round with the given id.
   */
  public Set<Opponent> getAssignedOpponents(Long roundId) {
    TypedQuery<PositionalOpponent> query = entityManager.createQuery("SELECT po "
            + "FROM Round r, Group g , PositionalOpponent po "
            + "WHERE r.id = :roundId "
            + "AND g MEMBER OF r.groups "
            + "AND po MEMBER OF g.positionalOpponents",
        PositionalOpponent.class);
    query.setParameter("roundId", roundId);
    List<PositionalOpponent> resultList = query.getResultList();

    Set<Opponent> assignedOpponents = new HashSet<>(resultList.size());
    for (PositionalOpponent po : resultList) {
      assignedOpponents.add(po.getOpponent());
    }

    return assignedOpponents;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Round groups
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Getting all groups which are assigned to the round with the given id.
   */
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

  /**
   * Adding the group with the given id to the round with the given id.
   */
  public Round newGroup(Long roundId, Long groupId) {
    Group group = entityManager.find(Group.class, groupId);
    Round round = read(roundId);

    round.getGroups().add(group);
    entityManager.persist(round);

    return round;
  }

  /**
   * Creating group and associate to a given round.
   *
   * @return List of all groups assigned to the round with the given id
   */
  public List<Group> newGroup(Long id) {
    Round round = this.read(id);

    Group group = new Group();
    round.appendGroup(group);
    entityManager.persist(group);

    entityManager.merge(round);
    entityManager.flush();

    return round.getGroups();
  }

  /**
   * Remove the group with the given id from the round with the given id.
   */
  public Round removeGroup(Long roundId, Long groupId) {
    Group group = entityManager.find(Group.class, groupId);
    Round round = read(roundId);

    round.getGroups().remove(group);
    entityManager.persist(round);

    return round;
  }

  /**
   * Getting the id of the round, to which the group with the given id is related.
   */
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

  /**
   * Getting the id of the round, to which the group with the given id is related
   * and has the given position.
   */
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

  /**
   * Getting the id of the round which is the processor of the round with the given id.
   */
  public Long getPrevRoundId(Long roundId) {
    Integer roundPosition = getPosition(roundId);
    Integer prevRoundPosition = roundPosition - 1;
    Long disciplineId = disciplineRepo.getDisciplineId(roundId);
    return getRoundId(disciplineId, prevRoundPosition);
  }

  /**
   * Getting the position of the round with the given id.
   */
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

  /**
   * Deleting a round with no opponents.
   *
   * @return List of all groups assigned to the round with the given id
   */
  public List<Group> deleteGroup(Long roundId) {
    Round round = this.read(roundId);
    List<Group> groups = round.getDeletableGroups();

    if (!groups.isEmpty()) {
      round.removeGroup(groups.get(0));

      entityManager.merge(round);
      entityManager.flush();
    }

    return round.getGroups();
  }
}
