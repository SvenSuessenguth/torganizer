package org.cc.torganizer.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.rest.container.PlayersContainer;

@Stateless
@Path("/players")
@Produces("application/json")
public class PlayersResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;
  
  
  @POST
  @Path("/create")
  public Player create(Player player) {
    player.setId(null);
    entityManager.persist(player);
    
    return player;
  }
  
  @GET
  @Path("{id}")
  public Player read(@PathParam("id") Long id) {

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", id);
    List<Player> players = namedQuery.getResultList();
    
    return players.get(0);
  }
  
  @POST
  @Path("/update")
  public Player update(Player player) {
    entityManager.merge(player);
    
    return player;
  }
  
  @DELETE
  @Path("/delete/{id}")
  public Player delete(@PathParam("id") Long id) {

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", id);
    List<Player> players = namedQuery.getResultList();    
    Player player = players.get(0);
    
    entityManager.remove(player);
    
    return player;
  }

  @GET
  @Path("/")
  public PlayersContainer all() {

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findAll", Player.class);
    List<Player> players = namedQuery.getResultList();
    
    return new PlayersContainer(players);
  }
  
  @GET
  @Path("/count")
  public long count() {
    Query query = entityManager.createQuery("SELECT count(*) FROM Player p");
    return (long) query.getSingleResult();
  }
}