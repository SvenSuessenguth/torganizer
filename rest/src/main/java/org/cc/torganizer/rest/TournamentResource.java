package org.cc.torganizer.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.rest.container.DisciplinesContainer;
import org.cc.torganizer.rest.container.OpponentsContainer;
import org.cc.torganizer.rest.container.PlayersContainer;
import org.cc.torganizer.rest.container.TournamentsContainer;

@Stateless
@Path("/tournaments")
@Produces("application/json")
public class TournamentResource extends AbstractResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @GET
  @Path("/create")
  public Tournament create(@QueryParam("name") String name) {

    Tournament tournament = new Tournament();
    tournament.setName(name);

    entityManager.persist(tournament);

    return tournament;
  }

  @GET
  @Path("{id}")
  public Tournament read(@PathParam("id") Long id) {

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findById", Tournament.class);
    namedQuery.setParameter("id", id);
    List<Tournament> tournaments = namedQuery.getResultList();

    return tournaments.get(0);
  }

  @GET
  @Path("/")
  public TournamentsContainer all(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {

    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findAll", Tournament.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    List<Tournament> tournaments = namedQuery.getResultList();

    return new TournamentsContainer(tournaments);
  }

  @GET
  @Path("/{id}/subscribers")
  public PlayersContainer subscribers(@PathParam("id") Long tournamentId) {
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Tournament.findSubscribers", Player.class);
    namedQuery.setParameter("id", tournamentId);
    List<Player> players = namedQuery.getResultList();

    return new PlayersContainer(players);
  }
  
  @GET
  @Path("/{id}/subscribers")
  public PlayersContainer subscribersFromTo(@PathParam("id") Long tournamentId, @QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }
    
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Tournament.findSubscribers", Player.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    
    List<Player> players = namedQuery.getResultList();

    return new PlayersContainer(players);
  }

  @GET
  @Path("/{tid}/subscribers/add/{pid}")
  public Player addSubscriber(@PathParam("tid") Long tournamentId, @PathParam("pid") Long playerId) {
    // load player
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", playerId);
    List<Player> players = namedQuery.getResultList();
    Player playerToAdd = players.get(0);

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery("Tournament.findById",
        Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    List<Tournament> tournaments = namedTournamentQuery.getResultList();
    Tournament tournament = tournaments.get(0);

    // persist tournament
    tournament.getSubscribers().add(playerToAdd);
    entityManager.persist(tournament);

    return playerToAdd;
  }
  
  @GET
  @Path("/{id}/subscribers/count")
  public Long subscribersCount(@PathParam("id") Long tournamentId) {
    Query query = entityManager.createNamedQuery("Tournament.countSubscribers");
    query.setParameter("id", tournamentId);
    
    return (long) query.getSingleResult();
  }
  
  @GET
  @Path("/{id}/opponents")
  public OpponentsContainer opponents(@PathParam("id") Long tournamentId) {
    TypedQuery<Opponent> namedQuery = entityManager.createNamedQuery("Tournament.findOpponents", Opponent.class);
    namedQuery.setParameter("id", tournamentId);
    List<Opponent> opponents = namedQuery.getResultList();

    return new OpponentsContainer(opponents);
  }

  @GET
  @Path("/{id}/opponents/add")
  public Opponent opponents(@PathParam("id") Long tournamentId, @QueryParam("opponentId") Long opponentId) {
    // load player
    TypedQuery<Opponent> namedQuery = entityManager.createNamedQuery("Opponent.findById", Opponent.class);
    namedQuery.setParameter("id", opponentId);
    List<Opponent> opponents = namedQuery.getResultList();
    Opponent opponentToAdd = opponents.get(0);

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery("Tournament.findById",
        Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    List<Tournament> tournaments = namedTournamentQuery.getResultList();
    Tournament tournament = tournaments.get(0);

    // persist tournament
    tournament.getOpponents().add(opponentToAdd);
    entityManager.persist(tournament);

    return opponentToAdd;
  }
  

  @GET
  @Path("/{id}/disciplines")
  public DisciplinesContainer disciplines(@PathParam("id") Long tournamentId) {
    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery("Tournament.findDisciplines", Discipline.class);
    namedQuery.setParameter("id", tournamentId);
    List<Discipline> disciplines = namedQuery.getResultList();

    return new DisciplinesContainer(disciplines);
  }

  @GET
  @Path("/{id}/disciplines/add")
  public Discipline disciplines(@PathParam("id") Long tournamentId, @QueryParam("disciplineId") Long disciplineId) {
    // load discipline
    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery("Discipline.findById", Discipline.class);
    namedQuery.setParameter("id", disciplineId);
    List<Discipline> disciplines = namedQuery.getResultList();
    Discipline disciplineToAdd = disciplines.get(0);

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery("Tournament.findById",
        Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    List<Tournament> tournaments = namedTournamentQuery.getResultList();
    Tournament tournament = tournaments.get(0);

    // persist tournament
    tournament.getDisciplines().add(disciplineToAdd);
    entityManager.persist(tournament);

    return disciplineToAdd;
  }
}