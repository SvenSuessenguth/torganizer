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
        super(entityManager);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Person CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Match create(final Match match) {
        getEntityManager().persist(match);
        // with no flush, the id is unknown
        getEntityManager().flush();

        return match;
    }

    public final Match read(final Long matchId) {
        return getEntityManager().find(Match.class, matchId);
    }

    public final List<Match> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Match> namedQuery = getEntityManager().createNamedQuery("Match.findAll", Match.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    public final Match update(final Match match) {
        getEntityManager().merge(match);

        return match;
    }

    public final Match delete(final Long matchId) {
        Match match = getEntityManager().find(Match.class, matchId);

        getEntityManager().remove(match);

        return match;
    }

    public final long count() {
        Query query = getEntityManager().createQuery("SELECT count(m) FROM Match m");
        return (long) query.getSingleResult();
    }
}
