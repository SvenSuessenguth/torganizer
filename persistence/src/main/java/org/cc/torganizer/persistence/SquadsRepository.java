package org.cc.torganizer.persistence;

import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Stateless
public class SquadsRepository extends Repository {

    public SquadsRepository() {
    }

    /**
     * Constructor for testing.
     *
     * @param entityManager EntityManager
     */
    SquadsRepository(final EntityManager entityManager) {
        super(entityManager);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Person CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Squad create(final Squad squad) {
        getEntityManager().persist(squad);
        getEntityManager().flush();

        return squad;
    }

    public final Squad read(final Long squadId) {
        return getEntityManager().find(Squad.class, squadId);
    }

    public final List<Squad> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Squad> namedQuery = getEntityManager().createNamedQuery("Squad.findAll", Squad.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);

        return namedQuery.getResultList();
    }

    public final List<Squad> readOrderByLastName(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Squad> namedQuery = getEntityManager().createNamedQuery("Squad.findAll", Squad.class);
        List<Squad> squads = namedQuery.getResultList();

        Collections.sort(squads, new OpponentByNameComparator());
        return squads.subList(offset, offset + maxResults);
    }

    public final Squad update(final Squad squad) {
        getEntityManager().merge(squad);
        return squad;
    }

    public final Squad delete(final Long squadId) {
        Squad squad = getEntityManager().find(Squad.class, squadId);

        getEntityManager().remove(squad);

        return squad;
    }

    public final long count() {
        Query query = getEntityManager().createQuery("SELECT count(s) FROM Squad s");
        return (long) query.getSingleResult();
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Squads players
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final List<Player> getPlayers(final Long squadId) {
        TypedQuery<Player> namedQuery = getEntityManager().createNamedQuery("Squad.findPlayers", Player.class);
        namedQuery.setParameter("id", squadId);

        return namedQuery.getResultList();
    }
}
