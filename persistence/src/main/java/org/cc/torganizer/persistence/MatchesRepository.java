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
    MatchesRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Person CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public Match create(Match match) {
        entityManager.persist(match);
        // with no flush, the id is unknown
        entityManager.flush();

        return match;
    }

    public Match read(Long matchId) {
        return entityManager.find(Match.class, matchId);
    }

    public List<Match> read(Integer offset, Integer maxResults) {
        offset = offset == null ? DEFAULT_OFFSET : offset;
        maxResults = maxResults == null ? DEFAULT_MAX_RESULTS : maxResults;

        TypedQuery<Match> namedQuery = entityManager.createNamedQuery("Match.findAll", Match.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    public Match update(Match match) {
        entityManager.merge(match);

        return match;
    }

    public Match delete(Long matchId) {
        Match match = entityManager.find(Match.class, matchId);

        entityManager.remove(match);

        return match;
    }

    public long count() {
        Query query = entityManager.createQuery("SELECT count(m) FROM Match m");
        return (long) query.getSingleResult();
    }
}
