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

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Status;
import org.cc.torganizer.rest.container.PlayersContainer;

@Stateless
@Path("/players")
@Produces("application/json")
public class PlayersResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;
  
  
  @GET
  @Path("/")
  public PlayersContainer all() {

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findAll", Player.class);
    List<Player> players = namedQuery.getResultList();
    
    return new PlayersContainer(players);
  }
  
  @GET
  @Path("{id}")
  public Player byId(@PathParam("id") Long id) {

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", id);
    List<Player> players = namedQuery.getResultList();
    
    return players.get(0);
  }
    
  
  @GET
  @Path("/new")
  public Player newPlayer(@QueryParam("personId") Long personId) {

    // read person
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findById", Person.class);
    namedQuery.setParameter("id", personId);
    List<Person> persons = namedQuery.getResultList();
    Person person = persons.get(0);
    
    // persist new player
    Player player = new Player(person);
    player.setStatus(Status.INACTIVE);    
    entityManager.persist(player);

    return player;
  }
}