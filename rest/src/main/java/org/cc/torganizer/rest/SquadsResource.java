package org.cc.torganizer.rest;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.cc.torganizer.core.entities.Squad;
import static org.cc.torganizer.rest.AbstractResource.DEFAULT_LENGTH;
import static org.cc.torganizer.rest.AbstractResource.DEFAULT_OFFSET;
import org.cc.torganizer.rest.json.SquadJsonConverter;

@Stateless
@Path("/squads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SquadsResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;
  
  @Inject
  private SquadJsonConverter converter;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    
    Squad squad = converter.toModel(jsonObject);
    
    entityManager.persist(squad);
    entityManager.flush();

    return converter.toJsonObject(squad);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findById", Squad.class);
    namedQuery.setParameter("id", id);
    Squad squad = namedQuery.getSingleResult();

    return  converter.toJsonObject(squad);
  }
  
  @GET
  public JsonArray readMultiple(@QueryParam("tournamentId") Long tournamentId, @QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {

    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findAllForTournament", Squad.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    List<Squad> squads = namedQuery.getResultList();

    return converter.toJsonArray(squads);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    final Squad squad = converter.toModel(jsonObject);
    entityManager.merge(squad);

    return  converter.toJsonObject(squad);
  }

  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Squad.findById", Squad.class);
    namedQuery.setParameter("id", id);
    Squad squad = namedQuery.getSingleResult();
    
    entityManager.remove(squad);

    return converter.toJsonObject(squad);
  }

  @GET
  @Path("/count")
  public long count() {
    Query query = entityManager.createQuery("SELECT count(s) FROM Squad s");
    return (long) query.getSingleResult();
  }
}