package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Group;
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
public class RoundsRepository extends Repository {

    public RoundsRepository() {
    }

    /**
     * Constructor for testing.
     *
     * @param entityManager EntityManager
     */
    RoundsRepository(final EntityManager entityManager) {
        super(entityManager);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Round CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Round create(final Round round) {
        // client can send '0' with a detached object exception as the result
        round.setId(null);

        getEntityManager().persist(round);
        getEntityManager().flush();

        return round;
    }

    public final Round read(final Long roundId) {
        return getEntityManager().find(Round.class, roundId);
    }

    public final List<Round> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Round> namedQuery = getEntityManager().createNamedQuery("Round.findAll", Round.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);

        return namedQuery.getResultList();
    }

    public final Round update(final Round round) {
        return getEntityManager().merge(round);
    }

    public final Round delete(final Long roundId) {
        Round round = getEntityManager().find(Round.class, roundId);

        getEntityManager().remove(round);

        return round;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Round groups
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final List<Group> getGroups(Long roundId, Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
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

        TypedQuery<Group> query = getEntityManager().createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(maxResults);

        return query.getResultList();

    }

    public final Round addGroup(final Long roundId, final Long groupId) {
        Group group = getEntityManager().find(Group.class, groupId);
        Round round = read(roundId);

        round.getGroups().add(group);
        getEntityManager().persist(round);

        return round;
    }

    public final Round removeGroup(final Long roundId, final Long groupId) {
        Group group = getEntityManager().find(Group.class, groupId);
        Round round = read(roundId);

        round.getGroups().remove(group);
        getEntityManager().persist(round);

        return round;
    }

    public final Long getRoundId(final Long groupId) {
        Long roundId = null;

        try {
            TypedQuery<Long> query = getEntityManager().createQuery("SELECT r.id FROM Round r, Group g WHERE g.id = :groupId AND g MEMBER OF r.groups", Long.class);
            query.setParameter("groupId", groupId);
            roundId = query.getSingleResult();
        } catch (NoResultException nrExc) {
            return null;
        }

        return roundId;
    }
}
