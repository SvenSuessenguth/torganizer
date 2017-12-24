package org.cc.torganizer.rest;

import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.cc.torganizer.core.entities.Player;
import static org.cc.torganizer.rest.AbstractResource.DEFAULT_LENGTH;
import static org.cc.torganizer.rest.AbstractResource.DEFAULT_OFFSET;
import org.cc.torganizer.rest.json.PlayerJsonConverter;

@Stateless
@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayersResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;
  
  @Inject
  private PlayerJsonConverter converter;

  @POST
  @Path("/create")
  public JsonObject create(JsonObject jsonObject) {
    
    Player player = converter.toModel(jsonObject);
    
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Player>> violations = validator.validate( player );
    
    entityManager.persist(player);

    return converter.toJsonObject(player);
  }

  @GET
  @Path("{id}")
  public JsonObject read(@PathParam("id") Long id) {

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", id);
    List<Player> players = namedQuery.getResultList();

    return  converter.toJsonObject(players.get(0));
  }

  @POST
  @Path("/update")
  public String update(Player player) {
    entityManager.merge(player);

    return  converter.toJsonObject(player).toString();
  }

  @DELETE
  @Path("/delete/{id}")
  public JsonObject delete(@PathParam("id") Long id) {

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", id);
    List<Player> players = namedQuery.getResultList();
    Player player = players.get(0);

    entityManager.remove(player);

    return converter.toJsonObject(player);
  }

  @GET
  public JsonArray all(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length,
      @QueryParam("tournamentId") Long tournamentId) {

    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findByTournament", Player.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    namedQuery.setParameter("tournamentId", tournamentId);
    List<Player> players = namedQuery.getResultList();

    return converter.toJsonArray(players);
  }

  @GET
  @Path("/count")
  public long count() {
    Query query = entityManager.createQuery("SELECT count(*) FROM Player p");
    return (long) query.getSingleResult();
  }
}