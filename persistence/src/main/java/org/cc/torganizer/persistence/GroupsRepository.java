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
import org.cc.torganizer.core.entities.Round;

@Stateless
public class GroupsRepository extends Repository {

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
  public Group create(Group group) {
    // Group wird als nicht-persistente entity betrachtet.
    // vom client kann die id '0' geliefert werden, sodass eine detached-entity-Exception geworfen
    // wird.
    group.setId(null);
    entityManager.persist(group);

    return group;
  }

  public Group read(Long groupId) {
    return entityManager.find(Group.class, groupId);
  }

  public List<Group> read(Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Group> namedQuery = entityManager.createNamedQuery("Group.findAll", Group.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public Group update(Group group) {
    entityManager.merge(group);

    return group;
  }

  public Group delete(Long groupId) {
    Group group = entityManager.find(Group.class, groupId);

    entityManager.remove(group);

    return group;
  }


  public long count() {
    Query query = entityManager.createQuery("SELECT count(g) FROM Group g");
    return (long) query.getSingleResult();
  }

  public Set<Opponent> getAssignableOpponents(Long groupId) {
    // an opponent is assignable, if he
    // - passed the previous round
    // - is not already assigned to a group in the current round
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
}
