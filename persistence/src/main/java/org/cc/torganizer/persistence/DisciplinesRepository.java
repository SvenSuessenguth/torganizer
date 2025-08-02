package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;

import java.util.List;

/**
 * Accessing the Repository for Disciplines and related entities.
 */
@RequestScoped
@NoArgsConstructor
@Log
public class DisciplinesRepository extends Repository<Discipline> {

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  DisciplinesRepository(EntityManager entityManager) {
    this.em = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Discipline CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Discipline read(Long disciplineId) {
    return em.find(Discipline.class, disciplineId);
  }

  @Override
  public List<Discipline> read(Integer offset, Integer maxResults) {
    throw new UnsupportedOperationException();
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Discipline opponents
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Getting the opponents related to the given discipline.
   */
  public List<Opponent> getOpponents(Long disciplineId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var cb = em.getCriteriaBuilder();
    var cq = cb.createQuery(Opponent.class);
    var discipline = cq.from(Discipline.class);
    var opponent = cq.from(Opponent.class);
    var disciplineOpponentJoin = discipline.join("opponents");

    cq.select(opponent);
    cq.where(
      cb.and(
        cb.equal(discipline.get("id"), disciplineId),
        cb.equal(disciplineOpponentJoin.get("id"), opponent.get("id"))
      )
    );

    var query = em.createQuery(cq);
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Discipline rounds
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Getting the rounds related to the discipline with the given id.
   */
  public List<Round> getRounds(Long disciplineId, Integer offset, Integer maxResults) {
    offset = offset == null ? DEFAULT_OFFSET : offset;
    maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

    var cb = em.getCriteriaBuilder();
    var cq = cb.createQuery(Round.class);
    var discipline = cq.from(Discipline.class);
    var round = cq.from(Round.class);
    var disciplineRoundJoin = discipline.join("rounds");

    cq.select(round);
    cq.where(
      cb.and(
        cb.equal(discipline.get("id"), disciplineId),
        cb.equal(disciplineRoundJoin.get("id"), round.get("id"))
      )
    );

    var query = em.createQuery(cq);
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();
  }

  /**
   * Adding the round with the give id to the discipline with the given id.
   */
  public Discipline addRound(Long disciplineId, Long roundId) {
    var round = em.find(Round.class, roundId);

    var discipline = read(disciplineId);
    discipline.addRound(round);
    em.persist(discipline);

    log.info("Position der Round nach hinzufügen zu Discipline: " + round.getPosition());

    return discipline;
  }

  /**
   * Rmoving the round with the give id to the discipline with the given id.
   */
  public Discipline removeRound(Long disciplineId, Long roundId) {
    var round = em.find(Round.class, roundId);

    var discipline = read(disciplineId);
    discipline.getRounds().remove(round);
    em.persist(discipline);

    return discipline;
  }

  /**
   * Getting the Discipline to which the round is related.
   */
  public Long getDisciplineId(Long roundId) {
    try {
      var query = em.createQuery("SELECT d.id "
        + "FROM Discipline d, Round r "
        + "WHERE r.id = :roundId "
        + "AND r MEMBER OF d.rounds", Long.class);
      query.setParameter("roundId", roundId);
      return query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }
  }

  /**
   * Getting the round with the given position from the discipline with the given id.
   */
  public Round getRoundByPosition(Long disciplineId, Integer position) {
    try {
      var query = em.createQuery("SELECT r "
        + "FROM Round r, Discipline d "
        + "WHERE r.position = :position "
        + "AND d.id = :disciplineId", Round.class);
      query.setParameter("disciplineId", disciplineId);
      query.setParameter("position", position);

      return query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }
  }

  public long count() {
    var query = em.createQuery("SELECT count(d) FROM Discipline d");
    return (long) query.getSingleResult();
  }
}
