package org.cc.torganizer.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.PositionalOpponent;
import org.cc.torganizer.core.entities.Round;

@Stateless
public class GroupsRepository extends Repository<Group> {

  @Inject
  private RoundsRepository roundsRep;

  @Inject
  private DisciplinesRepository disciplineRepo;

  public GroupsRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  GroupsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Group read(Long groupId) {
    return entityManager.find(Group.class, groupId);
  }

  @Override
  public List<Group> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Group> namedQuery = entityManager.createNamedQuery("Group.findAll", Group.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public long count() {
    Query query = entityManager.createQuery("SELECT count(g) FROM Group g");
    return (long) query.getSingleResult();
  }

  /**
   * Return the opponents, who are assignable to a group.
   * An opponent is assignable, if he
   * <ul>
   * <li>passed the previous round</li>
   * <li>is not already assigned to a group in the current round</li>
   * </ul>
   */
  public Set<Opponent> getAssignableOpponents(Long groupId) {
    Long roundId = roundsRep.getRoundId(groupId);
    Round round = roundsRep.read(roundId);
    Long disciplineId = disciplineRepo.getDisciplineId(roundId);
    Discipline discipline = disciplineRepo.read(disciplineId);

    // opponents in round of group (passed the previous round)
    Set<Opponent> opponentsInRound = null;
    if (round.getPosition() == 0) {
      opponentsInRound = discipline.getOpponents();
    } else {
      Round previousRound = discipline.getRound(round.getPosition() - 1);
      opponentsInRound = previousRound.getQualifiedOpponents();
    }

    // opponents not already assigned to group are assignable
    return filterAlreadyAssignedOpponents(round, opponentsInRound);
  }

  protected Set<Opponent> filterAlreadyAssignedOpponents(Round round,
                                                         Set<Opponent> opponentsInRound) {
    Set<Opponent> assignableOpponents = new HashSet<>();
    List<Group> groups = round.getGroups();
    for (Opponent candidate : opponentsInRound) {
      boolean isAssignable = true;
      for (Group group : groups) {
        if (group.getOpponents().contains(candidate)) {
          isAssignable = false;
          break;
        }
      }

      if (isAssignable) {
        assignableOpponents.add(candidate);
      }
    }
    return assignableOpponents;
  }

  /**
   * Adding an opponent with the given id to the group with the given id.
   */
  public Group addOpponent(Long groupId, Long opponentId) {
    Opponent opponent = entityManager.find(Opponent.class, opponentId);

    Group group = read(groupId);
    group.addOpponent(opponent);
    entityManager.persist(group);

    return group;
  }

  /**
   * Getting positional Opponents for a given group.
   */
  public List<PositionalOpponent> getPositionalOpponents(Long groupId, Integer offset,
                                                         Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    TypedQuery<PositionalOpponent> namedQuery = entityManager
        .createNamedQuery("Group.findPositionalOpponents", PositionalOpponent.class);
    namedQuery.setParameter("groupId", groupId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }
}