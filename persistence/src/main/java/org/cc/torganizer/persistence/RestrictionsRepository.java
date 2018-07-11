package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Restriction;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class RestrictionsRepository extends Repository {

    public RestrictionsRepository() {
    }

    /**
     * Constructor for testing.
     *
     * @param entityManager EntityManager
     */
    RestrictionsRepository(final EntityManager entityManager) {
        super(entityManager);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Person CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Restriction create(final Restriction restriction) {
        getEntityManager().persist(restriction);
        getEntityManager().flush();

        return restriction;
    }

    public final Restriction read(final Long restrictionId) {
        return getEntityManager().find(Restriction.class, restrictionId);
    }

    public final List<Restriction> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Restriction> namedQuery = getEntityManager().createNamedQuery("Restriction.findAll", Restriction.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);

        return namedQuery.getResultList();
    }
}
