package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.PositionalOpponent;
import org.cc.torganizer.core.entities.Round;

/**
 * Accessing the Repository for Groups and related entities.
 */
@RequestScoped
public class GroupsRepository extends Repository<Group> {

  @Inject
  private RoundsRepository roundsRep;

  @Inject
  private DisciplinesRepository disciplineRepo;

  @SuppressWarnings("unused")
  public GroupsRepository() {
  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  GroupsRepository(EntityManager entityManager) {
    this.em = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Group read(Long groupId) {
    return em.find(Group.class, groupId);
  }

  @Override
  public List<Group> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Group> namedQuery = em.createNamedQuery("Group.findAll", Group.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public long count() {
    var query = em.createQuery("SELECT count(g) FROM Group g");
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
  @SuppressWarnings("unused")
  public Set<Opponent> getAssignableOpponents(Long groupId) {
    var roundId = roundsRep.getRoundId(groupId);
    var round = roundsRep.read(roundId);
    var disciplineId = disciplineRepo.getDisciplineId(roundId);
    var discipline = disciplineRepo.read(disciplineId);

    // opponents in round of group (passed the previous round)
    Collection<Opponent> opponentsInRound;
    if (round.getPosition() == 0) {
      opponentsInRound = discipline.getOpponents();
    } else {
      var previousRound = discipline.getRound(round.getPosition() - 1);
      opponentsInRound = previousRound.getQualifiedOpponents();
    }

    // opponents not already assigned to group are assignable
    return filterAlreadyAssignedOpponents(round, opponentsInRound);
  }

  protected Set<Opponent> filterAlreadyAssignedOpponents(Round round,
                                                         Collection<Opponent> opponentsInRound) {
    var assignableOpponents = new HashSet<Opponent>();
    var groups = round.getGroups();
    for (var candidate : opponentsInRound) {
      var isAssignable = true;
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
    var opponent = em.find(Opponent.class, opponentId);

    var group = read(groupId);
    group.addOpponent(opponent);
    em.persist(group);

    return group;
  }

  /**
   * Getting positional Opponents for a given group.
   */
  public List<PositionalOpponent> getPositionalOpponents(Long groupId, Integer offset,
                                                         Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var namedQuery = em
        .createNamedQuery("Group.findPositionalOpponents", PositionalOpponent.class);
    namedQuery.setParameter("groupId", groupId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);

    return namedQuery.getResultList();
  }
}