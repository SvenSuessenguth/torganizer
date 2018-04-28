package org.cc.torganizer.rest;

import org.cc.torganizer.persistence.RoundsRepository;
import org.cc.torganizer.rest.json.RoundJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;

@Stateless
@Path("/rounds")
@Produces("application/json")
public class RoundsResource extends AbstractResource {

  @Inject
  private RoundsRepository rRepository;

  @Inject
  private RoundJsonConverter dConverter;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    return null;
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long disciplineId) {
    return null;
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    return null;
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    return null;
  }

  @GET
  @Path("/{id}/opponents")
  public JsonArray getOpponents(@PathParam("id") Long disciplineId, @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    return null;
  }

  @POST
  @Path("/{id}/opponents")
  public JsonObject addOpponent(@PathParam("id") Long disciplineId, @QueryParam("opponentId") Long opponentId) {
    return null;
  }

  @DELETE
  @Path("/{id}/opponents")
  public JsonObject removeOpponent(@PathParam("id") Long disciplineId, @QueryParam("opponentId") Long opponentId) {
    return null;
  }
}
