package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;

@Stateless
public class GroupsRepository extends Repository{

  @Inject
  private RoundsRepository rRepository;

  @Inject
  private DisciplinesRepository dRepository;

  public GroupsRepository() {
  }

  /**
   * Constructor for testing.
   */
  public GroupsRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Person CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Group create(Group group){
    // Group wird als nicht-persistente entity betrachtet.
    // vom client kann die id '0' geliefert werden, sodass eine detached-entity-Exception geworfen wird.
    group.setId(null);
    entityManager.persist(group);

    return group;
  }

  public Group read(Long groupId){
    return entityManager.find(Group.class, groupId);
  }

  public List<Group> read(Integer offset, Integer maxResults){
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;


    TypedQuery<Group> namedQuery = entityManager.createNamedQuery("Group.findAll", Group.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public Group update(Group group){
    entityManager.merge(group);

    return group;
  }

  public Group delete(Long groupId){
    Group group = entityManager.find(Group.class, groupId);

    entityManager.remove(group);

    return group;
  }


  public long count() {
    Query query = entityManager.createQuery("SELECT count(g) FROM Group g");
    return (long) query.getSingleResult();
  }

  public List<Opponent> getAssignableOpponents(Long groupId){
    List<Opponent> assignableOpponents = null;

    // an opponent is assignable, if he
    // - passed the previous round
    // - is not already assigned to a group in the current round
    Long roundId = rRepository.getRoundId(groupId);
    Long disciplineId = dRepository.getDisciplineId(roundId);
    
    return assignableOpponents;
  }
}
