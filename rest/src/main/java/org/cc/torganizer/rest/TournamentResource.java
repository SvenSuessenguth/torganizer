package org.cc.torganizer.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.rest.container.PlayersContainer;
import org.cc.torganizer.rest.container.TournamentsContainer;

@Stateless
@Path("/tournaments")
@Produces("application/json")
public class TournamentResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;
  
  @GET
  @Path("/")
  public TournamentsContainer all() {

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findAll", Tournament.class);
    List<Tournament> tournaments = namedQuery.getResultList();
    
    return new TournamentsContainer(tournaments);
  }
  
  @GET
  @Path("{id}")
  public Tournament byId(@PathParam("id") Long id) {

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findById", Tournament.class);
    namedQuery.setParameter("id", id);
    List<Tournament> tournaments = namedQuery.getResultList();
    
    return tournaments.get(0);
  }
  
  @GET
  @Path("/new")
  public Tournament newTournament(@QueryParam("name") String name) {

    Tournament tournament = new Tournament();
    tournament.setName(name);
    
    entityManager.persist(tournament);
    
    return tournament;
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
  @Path("/{id}/subscribers/add")
  public Player subscribers(@PathParam("id") Long tournamentId, @QueryParam("playerId") Long playerId) {
    // load player
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", playerId);
    List<Player> players = namedQuery.getResultList();
    Player playerToAdd = players.get(0);
    
    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery("Tournament.findById", Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    List<Tournament> tournaments = namedTournamentQuery.getResultList();
    Tournament tournament = tournaments.get(0);
    
    // persist tournament
    tournament.getSubscribers().add(playerToAdd);
    entityManager.persist(tournament);
    
    return playerToAdd;    
  }
}