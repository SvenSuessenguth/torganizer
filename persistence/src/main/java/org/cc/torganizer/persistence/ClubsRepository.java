package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Club;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class ClubsRepository extends Repository {

    public ClubsRepository() {
    }

    /**
     * Constructor for testing purpose.
     *
     * @param entityManager EntityManager
     */
    ClubsRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    //--------------------------------------------------------------------------------------------------------------------
    //
    // Tournaments CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Club create(final Club club) {
        entityManager.persist(club);
        entityManager.flush();

        return club;
    }

    public final Club read(final Long clubId) {
        return entityManager.find(Club.class, clubId);
    }

    public final List<Club> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Club> namedQuery = entityManager.createNamedQuery("Club.findAll", Club.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    public final Club update(final Club club) {
        entityManager.merge(club);

        return club;
    }

    public final Club delete(final Club club) {
        entityManager.remove(club);

        return club;
    }
}
