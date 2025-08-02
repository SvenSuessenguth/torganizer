package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.PositionalOpponent;
import org.cc.torganizer.core.entities.Round;

/**
 * Accessing the Repository for Rounds and related entities.
 */
@RequestScoped
public class RoundsRepository extends Repository<Round> {

  @Inject
  private DisciplinesRepository disciplineRepo;

  @SuppressWarnings("unused")
  public RoundsRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  RoundsRepository(EntityManager entityManager, DisciplinesRepository disciplineRepo) {
    this.em = entityManager;
    this.disciplineRepo = disciplineRepo;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Round CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Round read(Long roundId) {
    return em.find(Round.class, roundId);
  }

  @Override
  public List<Round> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var namedQuery = em.createNamedQuery("Round.findAll", Round.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }

  /**
   * Reading the opponents which are related to the round with the given id.
   */
  public Collection<Opponent> getOpponents(Long roundId) {
    var prevRoundId = getPrevRoundId(roundId);
    Collection<Opponent> opponents;

    if (prevRoundId != null) {
      var prevRound = read(prevRoundId);
      opponents = prevRound.getQualifiedOpponents();
    } else {
      var disciplineId = disciplineRepo.getDisciplineId(roundId);
      opponents = new HashSet<>(disciplineRepo.getOpponents(disciplineId, 0, 999));
    }

    return opponents;
  }

  /**
   * Find all opponents, which are not already assigned to a group of the round.
   */
  public Collection<Opponent> getNotAssignedOpponents(Long roundId, Integer offset,
                                                      Integer maxResults) {

    var opponents = getOpponents(roundId);
    var assignedOpponents = getAssignedOpponents(roundId);

    // sets can't work with positions
    var notAssignedOpponents = new ArrayList<>(opponents);
    notAssignedOpponents.removeAll(assignedOpponents);
    notAssignedOpponents.sort(new OpponentByNameComparator());

    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;
    var foundOpponentsSize = Integer.valueOf(notAssignedOpponents.size());
    maxResults = maxResults > foundOpponentsSize ? foundOpponentsSize : maxResults;

    return new HashSet<>(notAssignedOpponents.subList(offset, maxResults));
  }

  /**
   * Getting all opponents which are assigned to the round with the given id.
   */
  public Set<Opponent> getAssignedOpponents(Long roundId) {
    var query = em.createQuery("SELECT po "
            + "FROM Round r, Group g , PositionalOpponent po "
            + "WHERE r.id = :roundId "
            + "AND g MEMBER OF r.groups "
            + "AND po MEMBER OF g.positionalOpponents",
        PositionalOpponent.class);
    query.setParameter("roundId", roundId);
    var resultList = query.getResultList();

    var assignedOpponents = new HashSet<Opponent>(resultList.size());
    for (var po : resultList) {
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

    var cb = em.getCriteriaBuilder();
    var cq = cb.createQuery(Group.class);
    var round = cq.from(Round.class);
    var group = cq.from(Group.class);
    var roundGroupJoin = round.join("groups");

    cq.select(group);
    cq.where(
        cb.and(
            cb.equal(round.get("id"), roundId),
            cb.equal(roundGroupJoin.get("id"), group.get("id"))
        )
    );

    var query = em.createQuery(cq);
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();

  }

  /**
   * Creating group and associate to a given round.
   *
   * @return List of all groups assigned to the round with the given id
   */
  public List<Group> newGroup(Long id) {
    var round = this.read(id);

    var group = new Group();
    round.appendGroup(group);
    em.persist(group);

    em.merge(round);
    em.flush();

    return round.getGroups();
  }

  /**
   * Getting the id of the round, to which the group with the given id is related.
   */
  public Long getRoundId(Long groupId) {
    try {
      var query = em.createQuery("SELECT r.id FROM Round r, Group g "
          + "WHERE g.id = :groupId "
          + "AND g MEMBER OF r.groups", Long.class);
      query.setParameter("groupId", groupId);

      return query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }
  }

  /**
   * Getting the id of the round, to which the group with the given id is related
   * and has the given position.
   */
  public Long getRoundId(Long disciplineId, Integer roundPosition) {
    try {
      var query = em.createQuery("SELECT r.id FROM Round r, Discipline d "
          + "WHERE d.id = :disciplineId "
          + "AND r MEMBER OF d.rounds "
          + "AND r.position = :roundPosition", Long.class);
      query.setParameter("disciplineId", disciplineId);
      query.setParameter("roundPosition", roundPosition);
      return query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }
  }

  /**
   * Getting the id of the round which is the processor of the round with the given id.
   */
  public Long getPrevRoundId(Long roundId) {
    var roundPosition = getPosition(roundId);
    var prevRoundPosition = roundPosition - 1;
    var disciplineId = disciplineRepo.getDisciplineId(roundId);

    return getRoundId(disciplineId, prevRoundPosition);
  }

  /**
   * Getting the position of the round with the given id.
   */
  public Integer getPosition(Long roundId) {
    try {
      var query = em.createQuery("SELECT r.position FROM Round r "
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
  @SuppressWarnings("unused")
  public List<Group> deleteGroup(Long roundId) {
    var round = this.read(roundId);
    var groups = round.getDeletableGroups();

    if (!groups.isEmpty()) {
      round.removeGroup(groups.getFirst());

      em.merge(round);
      em.flush();
    }

    return round.getGroups();
  }
}
