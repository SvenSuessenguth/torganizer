package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Match;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MatchesRepository extends Repository {

    public MatchesRepository() {
    }

    /**
     * Constructor for testing.
     *
     * @param entityManager EntityManager
     */
    MatchesRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Person CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Match create(final Match match) {
        entityManager.persist(match);
        // with no flush, the id is unknown
        entityManager.flush();

        return match;
    }

    public final Match read(final Long matchId) {
        return entityManager.find(Match.class, matchId);
    }

    public List<Match> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Match> namedQuery = entityManager.createNamedQuery("Match.findAll", Match.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    public final Match update(final Match match) {
        entityManager.merge(match);

        return match;
    }

    public final Match delete(final Long matchId) {
        Match match = entityManager.find(Match.class, matchId);

        entityManager.remove(match);

        return match;
    }

    public final long count() {
        Query query = entityManager.createQuery("SELECT count(m) FROM Match m");
        return (long) query.getSingleResult();
    }
}
