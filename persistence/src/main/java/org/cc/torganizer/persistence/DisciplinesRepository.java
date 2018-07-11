package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.core.entities.Round;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class DisciplinesRepository extends Repository {

    private static final String DISCIPLINE_FIND_ALL_QUERY_NAME = "Discipline.findAll";

    public DisciplinesRepository() {
    }

    /**
     * Constructor for testing.
     *
     * @param entityManager EntityManager
     */
    DisciplinesRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Discipline CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Discipline create(final Discipline discipline) {
        // client can send '0' with a detached object exception as the result
        discipline.setId(null);
        discipline.getRestrictions().forEach((Restriction restriction) -> restriction.setId(null));

        entityManager.persist(discipline);
        entityManager.flush();

        return discipline;
    }

    public final Discipline read(final Long disciplineId) {
        return entityManager.find(Discipline.class, disciplineId);
    }

    public final List<Discipline> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_ALL_QUERY_NAME, Discipline.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    public final Discipline update(final Discipline discipline) {
        return entityManager.merge(discipline);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Discipline opponents
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final List<Opponent> getOpponents(final Long disciplineId, Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
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

    public final Discipline addOpponent(final Long disciplineId, final Long opponentId) {
        Opponent opponent = entityManager.find(Opponent.class, opponentId);

        Discipline discipline = read(disciplineId);
        discipline.getOpponents().add(opponent);
        entityManager.persist(discipline);

        return discipline;
    }

    public final Discipline removeOpponent(final Long disciplineId, final Long opponentId) {
        Opponent opponent = entityManager.find(Opponent.class, opponentId);

        Discipline discipline = read(disciplineId);
        discipline.getOpponents().remove(opponent);
        entityManager.persist(discipline);

        return discipline;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Discipline rounds
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final List<Round> getRounds(final Long disciplineId, Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
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

    public final Discipline addRound(final Long disciplineId, final Long roundId) {
        Round round = entityManager.find(Round.class, roundId);

        Discipline discipline = read(disciplineId);
        discipline.addRound(round);
        entityManager.persist(discipline);

        return discipline;
    }

    public final Discipline removeRound(final Long disciplineId, final Long roundId) {
        Round round = entityManager.find(Round.class, roundId);

        Discipline discipline = read(disciplineId);
        discipline.getRounds().remove(round);
        entityManager.persist(discipline);

        return discipline;
    }

    public final Long getDisciplineId(final Long roundId) {
        Long disciplineId = null;

        try {
            TypedQuery<Long> query = entityManager.createQuery("SELECT d.id FROM Discipline d, Round r WHERE r.id = :roundId AND r MEMBER OF d.rounds", Long.class);
            query.setParameter("roundId", roundId);
            disciplineId = query.getSingleResult();
        } catch (NoResultException nrExc) {
            return null;
        }

        return disciplineId;
    }
}
