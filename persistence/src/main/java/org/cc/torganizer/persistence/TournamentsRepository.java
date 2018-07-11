package org.cc.torganizer.persistence;

import org.cc.torganizer.core.comparators.OpponentByNameComparator;
import org.cc.torganizer.core.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class TournamentsRepository extends Repository {

    /**
     * Constructor for testing purpose.
     *
     * @param entityManager EntityManager
     */
    TournamentsRepository(final EntityManager entityManager) {
        super(entityManager);
    }


    //--------------------------------------------------------------------------------------------------------------------
    //
    // Tournaments CRUD
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Tournament create(final Tournament tournament) {
        getEntityManager().persist(tournament);
        getEntityManager().flush();

        return tournament;
    }

    public final Tournament read(final Long tournamentId) {
        return getEntityManager().find(Tournament.class, tournamentId);
    }

    public final Tournament update(final Tournament tournament) {
        getEntityManager().merge(tournament);

        return tournament;
    }

    public final Tournament delete(final Tournament tournament) {
        getEntityManager().remove(tournament);

        return tournament;
    }


    public final List<Tournament> getTournaments(Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Tournament> namedQuery = getEntityManager().createNamedQuery("Tournament.findAll", Tournament.class);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        return namedQuery.getResultList();
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Tournaments players
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final List<Player> getPlayersOrderedByLastName(final Long tournamentId, Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Player> cq = cb.createQuery(Player.class);
        Root<Tournament> tournament = cq.from(Tournament.class);
        Root<Player> player = cq.from(Player.class);
        Join<Tournament, Player> tournamentOpponentJoin = tournament.join("opponents");

        cq.select(player);
        cq.where(
                cb.and(
                        cb.equal(tournament.get("id"), tournamentId),
                        cb.equal(tournamentOpponentJoin.type(), Player.class),
                        cb.equal(player.get("id"), tournamentOpponentJoin.get("id"))
                )
        );

        cq.orderBy(
                cb.asc(
                        player.get("person").get("lastName")
                )
        );

        TypedQuery<Player> query = getEntityManager().createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(maxResults);

        return query.getResultList();
    }

    public final Player addPlayer(final Long tournamentId, final Long playerId) {
        Player player = getEntityManager().find(Player.class, playerId);
        Tournament tournament = getEntityManager().find(Tournament.class, tournamentId);

        // persist tournament
        Set<Opponent> opponents = tournament.getOpponents();
        opponents.add(player);
        getEntityManager().persist(tournament);
        // to get the id
        getEntityManager().flush();

        return player;
    }

    public final Player removePlayer(final Long tournamentId, final Long playerId) {
        Player player = getEntityManager().find(Player.class, playerId);
        Tournament tournament = getEntityManager().find(Tournament.class, tournamentId);

        // persist tournament
        tournament.getOpponents().remove(player);
        getEntityManager().persist(tournament);
        // to get the id
        getEntityManager().flush();

        return player;
    }

    public final long countPlayers(final Long tournamentId) {
        Query query = getEntityManager().createNamedQuery("Tournament.countPlayers");
        query.setParameter("id", tournamentId);

        return (long) query.getSingleResult();
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Tournaments squads
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final Squad addSquad(final Long tournamentId, final Long squadId) {
        Squad squad = getEntityManager().find(Squad.class, squadId);
        Tournament tournament = getEntityManager().find(Tournament.class, tournamentId);

        // persist tournament
        tournament.getOpponents().add(squad);
        getEntityManager().persist(tournament);

        return squad;
    }

    public final List<Squad> getSquads(final Long tournamentId, Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Squad> namedQuery = getEntityManager().createNamedQuery("Tournament.findSquads", Squad.class);
        namedQuery.setParameter("id", tournamentId);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);
        List<Squad> squads = namedQuery.getResultList();
        squads.sort(new OpponentByNameComparator());

        return squads;
    }

    public final long countSquads(final Long tournamentId) {
        Query query = getEntityManager().createNamedQuery("Tournament.countSquads");
        query.setParameter("id", tournamentId);

        return (long) query.getSingleResult();
    }

    //--------------------------------------------------------------------------------------------------------------------
    //
    // Tournaments disciplines
    //
    //--------------------------------------------------------------------------------------------------------------------
    public final List<Discipline> getDisciplines(final Long tournamentId, Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        TypedQuery<Discipline> namedQuery = getEntityManager().createNamedQuery("Tournament.findDisciplines", Discipline.class);
        namedQuery.setParameter("id", tournamentId);
        namedQuery.setFirstResult(offset);
        namedQuery.setMaxResults(maxResults);

        return namedQuery.getResultList();
    }

    public final Discipline addDiscipline(final Long tournamentId, final Discipline discipline) {
        Tournament tournament = getEntityManager().find(Tournament.class, tournamentId);

        // persist tournament
        tournament.getDisciplines().add(discipline);
        getEntityManager().persist(tournament);

        return discipline;
    }

    public final List<Opponent> getOpponentsForDiscipline(final Long tournamentId, final Discipline discipline, Integer offset, Integer maxResults) {
        offset = getOffsetToUse(offset);
        maxResults = getMaxResultsToUse(maxResults);

        // load tournaments opponents
        Tournament tournament = getEntityManager().find(Tournament.class, tournamentId);
        Set<Opponent> opponents = tournament.getOpponents();

        // filter opponents
        List<Opponent> assignableOpponents = opponents
                .stream()
                .filter(discipline::isAssignable)
                .collect(Collectors.toList());

        // sort and use offset/length
        assignableOpponents.sort(new OpponentByNameComparator());

        // check offset/maxResults
        int size = assignableOpponents.size();
        if (offset > size) {
            offset = size;
        }
        if (offset + maxResults > size) {
            maxResults = size - offset;
        }

        return assignableOpponents.subList(offset, offset + maxResults);
    }
}
