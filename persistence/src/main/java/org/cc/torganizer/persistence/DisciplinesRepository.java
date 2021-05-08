package org.cc.torganizer.persistence;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;

/**
 * Accessing the Repository for Disciplines and related entities.
 */
@RequestScoped
public class DisciplinesRepository extends Repository<Discipline> {

  private static final Logger LOGGER = Logger.getLogger(DisciplinesRepository.class.getName());

  public DisciplinesRepository() {

  }

  /**
   * Constructor for testing.
   *
   * @param entityManager EntityManager
   */
  DisciplinesRepository(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Discipline CRUD
  //
  //-----------------------------------------------------------------------------------------------
  @Override
  public Discipline read(Long disciplineId) {
    return entityManager.find(Discipline.class, disciplineId);
  }

  @Override
  public List<Discipline> read(Integer offset, Integer maxResults) {
    return new ArrayList<>();
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

    var cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Opponent> cq = cb.createQuery(Opponent.class);
    Root<Discipline> discipline = cq.from(Discipline.class);
    Root<Opponent> opponent = cq.from(Opponent.class);
    Join<Discipline, Opponent> disciplineOpponentJoin = discipline.join("opponents");

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

  /**
   * Adding the opponent with the give id to the discipline with the given id.
   */
  public Discipline addOpponent(Long disciplineId, Long opponentId) {
    var opponent = entityManager.find(Opponent.class, opponentId);

    var discipline = read(disciplineId);
    discipline.getOpponents().add(opponent);
    entityManager.persist(discipline);

    return discipline;
  }

  /**
   * Removing the opponent with the give id from the discipline with the given id.
   */
  public Discipline removeOpponent(Long disciplineId, Long opponentId) {
    var opponent = entityManager.find(Opponent.class, opponentId);

    Discipline discipline = read(disciplineId);
    discipline.getOpponents().remove(opponent);
    entityManager.persist(discipline);

    return discipline;
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

    var cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Round> cq = cb.createQuery(Round.class);
    Root<Discipline> discipline = cq.from(Discipline.class);
    Root<Round> round = cq.from(Round.class);
    Join<Discipline, Round> disciplineRoundJoin = discipline.join("rounds");

    cq.select(round);
    cq.where(
        cb.and(
            cb.equal(discipline.get("id"), disciplineId),
            cb.equal(disciplineRoundJoin.get("id"), round.get("id"))
        )
    );

    TypedQuery<Round> query = entityManager.createQuery(cq);
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();
  }

  /**
   * Adding the round with the give id to the discipline with the given id.
   */
  public Discipline addRound(Long disciplineId, Long roundId) {
    var round = entityManager.find(Round.class, roundId);

    var discipline = read(disciplineId);
    discipline.addRound(round);
    entityManager.persist(discipline);

    LOGGER.info("Position der Round nach hinzufügen zu Discipline: " + round.getPosition());

    return discipline;
  }

  /**
   * Rmoving the round with the give id to the discipline with the given id.
   */
  public Discipline removeRound(Long disciplineId, Long roundId) {
    var round = entityManager.find(Round.class, roundId);

    var discipline = read(disciplineId);
    discipline.getRounds().remove(round);
    entityManager.persist(discipline);

    return discipline;
  }

  /**
   * Getting the Discipline to which the round is related.
   */
  public Long getDisciplineId(Long roundId) {
    Long disciplineId;

    try {
      TypedQuery<Long> query = entityManager.createQuery("SELECT d.id "
          + "FROM Discipline d, Round r "
          + "WHERE r.id = :roundId "
          + "AND r MEMBER OF d.rounds", Long.class);
      query.setParameter("roundId", roundId);
      disciplineId = query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }

    return disciplineId;
  }

  /**
   * Getting the round with the given position from the discipline with the given id.
   */
  public Round getRoundByPosition(Long disciplineId, Integer position) {
    Round round;

    try {
      TypedQuery<Round> query = entityManager.createQuery("SELECT r "
          + "FROM Round r, Discipline d "
          + "WHERE r.position = :position "
          + "AND d.id = :disciplineId", Round.class);
      query.setParameter("disciplineId", disciplineId);
      query.setParameter("position", position);
      round = query.getSingleResult();
    } catch (NoResultException nrExc) {
      return null;
    }

    return round;
  }

  public long count() {
    var query = entityManager.createQuery("SELECT count(d) FROM Discipline d");
    return (long) query.getSingleResult();
  }
}
