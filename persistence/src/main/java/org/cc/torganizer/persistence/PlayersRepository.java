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
        this.entityManager = entityManager;
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Person CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Player create(final Player player) {
        entityManager.persist(player);
        // with no flush, the id is unknown
        entityManager.flush();

        return player;
    }

    public final Player read(final Long playerId) {
        return entityManager.find(Player.class, playerId);
    }

    public final List<Player> read(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findAll", Player.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    public final Player update(final Player player) {
        entityManager.merge(player);

        return player;
    }

    public final Player delete(final Long playerId) {
        Player player = entityManager.find(Player.class, playerId);

        entityManager.remove(player);

        return player;
    }

    public final long count() {
        Query query = entityManager.createQuery("SELECT count(p) FROM Player p");
        return (long) query.getSingleResult();
    }
}
