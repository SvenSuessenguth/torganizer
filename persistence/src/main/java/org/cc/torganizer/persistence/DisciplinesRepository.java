package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Restriction;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class DisciplinesRepository extends Repository{

  private static final String DISCIPLINE_FIND_BY_ID_QUERY_NAME = "Discipline.findById";
  private static final String DISCIPLINE_FIND_ALL_QUERY_NAME = "Discipline.findAll";

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  public DisciplinesRepository() {
  }

  /**
   * Constructor for testing.
   */
  public DisciplinesRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Discipline CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Discipline create(Discipline discipline){
    // client can send '0' with a detached object exception as the result
    discipline.setId(null);
    discipline.getRestrictions().forEach((Restriction restriction) -> restriction.setId(null));

    entityManager.persist(discipline);
    entityManager.flush();

    return discipline;
  }

  public Discipline read(Long disciplineId){
    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_BY_ID_QUERY_NAME, Discipline.class);
    namedQuery.setParameter("id", disciplineId);

    return namedQuery.getSingleResult();
  }

  public List<Discipline> read(Integer offset, Integer maxResults){
    if (offset == null || maxResults == null) {
      offset = DEFAULT_OFFSET;
      maxResults = DEFAULT_MAX_RESULTS;
    }

    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_ALL_QUERY_NAME, Discipline.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  public Discipline update(Discipline discipline){
    return entityManager.merge(discipline);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Discipline opponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  public List<Opponent> getOpponents(Long disciplineId, Integer offset, Integer maxResults){
    if (offset == null || maxResults == null) {
      offset = DEFAULT_OFFSET;
      maxResults = DEFAULT_MAX_RESULTS;
    }

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Opponent> cq = cb.createQuery(Opponent.class);
    Root<Discipline> discipline = cq.from(Discipline.class);
    Root<Opponent> opponent = cq.from(Opponent.class);
    Join<Discipline, Opponent> disciplineOpponentJoin = discipline.join ("opponents");

    cq.select(opponent);
    cq.where(
      cb.and(
        cb.equal(discipline.get("id"), disciplineId),
        cb.equal(disciplineOpponentJoin.get("id"), opponent.get("id"))
      )
    );

    TypedQuery<Opponent> query = entityManager.createQuery(cq);
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();
  }

  public Discipline addOpponent(Long disciplineId, Opponent opponent){
    Discipline discipline = read(disciplineId);
    discipline.getOpponents().add(opponent);
    entityManager.persist(discipline);

    return discipline;
  }
}
