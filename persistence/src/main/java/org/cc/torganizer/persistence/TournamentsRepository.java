package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Stateless
public class TournamentsRepository extends Repository{

  private static final String TOURNAMENT_FIND_BY_ID_QUERY_NAME = "Tournament.findById";

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  public TournamentsRepository(){
  }

  /**
   * Constructor for testing purpose.
   */
  public TournamentsRepository(EntityManager entityManager){
    this.entityManager = entityManager;
  }


  //--------------------------------------------------------------------------------------------------------------------
  //
  // Tournaments
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Tournament getTournament(Long tournamentId){
    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME, Tournament.class);
    namedQuery.setParameter("id", tournamentId);
    return namedQuery.getSingleResult();
  }

  public List<Tournament> getTournaments(Integer offset, Integer maxResults){
    if (offset == null || maxResults == null) {
      offset = DEFAULT_OFFSET;
      maxResults = DEFAULT_MAX_RESULTS;
    }

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findAll", Tournament.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(maxResults);
    return namedQuery.getResultList();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Tournaments players
  //
  //--------------------------------------------------------------------------------------------------------------------
  public List<Player> getPlayersOrderedByLastName(Long tournamentId, Integer offset, Integer maxResults){
    if (offset == null || maxResults == null) {
      offset = DEFAULT_OFFSET;
      maxResults = DEFAULT_MAX_RESULTS;
    }

    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Player> cq = cb.createQuery(Player.class);
    Root<Tournament> tournament = cq.from(Tournament.class);
    Root<Player> player = cq.from(Player.class);
    Join<Tournament, Player> tournamentOpponentJoin = tournament.join ("opponents");

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

    TypedQuery<Player> query = entityManager.createQuery(cq);
    query.setFirstResult(offset);
    query.setMaxResults(maxResults);

    return query.getResultList();
  }

  public Player addPlayer(Long tournamentId, Long playerId){
    // load player
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", playerId);
    Player player = namedQuery.getSingleResult();

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME,
            Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    Tournament tournament = namedTournamentQuery.getSingleResult();

    // persist tournament
    Set<Opponent> opponents = tournament.getOpponents();
    opponents.add(player);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    return player;
  }

  public long countPlayers(Long tournamentId){
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", tournamentId);

    return (long) query.getSingleResult();
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Tournaments squads
  //
  //--------------------------------------------------------------------------------------------------------------------
  public Squad addSquad(Long tournamentId, Long squadId){
    // load player
    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Opponent.findById", Squad.class);
    namedQuery.setParameter("id", squadId);
    Squad squad = namedQuery.getSingleResult();

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME,
            Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    Tournament tournament = namedTournamentQuery.getSingleResult();

    // persist tournament
    tournament.getOpponents().add(squad);
    entityManager.persist(tournament);

    return squad;
  }
}
