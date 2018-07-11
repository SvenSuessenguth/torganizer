package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Player;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class PlayersRepository extends Repository {

    public PlayersRepository() {
    }

    /**
     * Constructor for testing.
     *
     * @param entityManager EntityManager
     */
    PlayersRepository(final EntityManager entityManager) {
        super(entityManager);
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Person CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Player create(final Player player) {
        getEntityManager().persist(player);
        // with no flush, the id is unknown
        getEntityManager().flush();

        return player;
    }

    public final Player read(final Long playerId) {
        return getEntityManager().find(Player.class, playerId);
    }

    public final List<Player> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Player> namedQuery = getEntityManager().createNamedQuery("Player.findAll", Player.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    public final Player update(final Player player) {
        getEntityManager().merge(player);

        return player;
    }

    public final Player delete(final Long playerId) {
        Player player = getEntityManager().find(Player.class, playerId);

        getEntityManager().remove(player);

        return player;
    }

    public final long count() {
        Query query = getEntityManager().createQuery("SELECT count(p) FROM Player p");
        return (long) query.getSingleResult();
    }
}
