package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.persistence.GroupsRepository;
import org.cc.torganizer.rest.json.GroupJsonConverter;
import org.cc.torganizer.rest.json.ModelJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import java.util.Set;

import static org.cc.torganizer.rest.json.ModelJsonConverter.emptyArray;

@Stateless
@Path("/groups")
@Produces("application/json")
public class GroupsResource extends AbstractResource {

  @Inject
  private GroupsRepository gRepository;

  @Inject
  private GroupJsonConverter gConverter;

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    return null;
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long groupId) {
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
  public JsonArray getOpponents(@PathParam("id") Long groupId, @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    return null;
  }

  @POST
  @Path("/{id}/opponents")
  public JsonObject addOpponent(@PathParam("id") Long groupId, @QueryParam("opponentId") Long opponentId) {
    return null;
  }

  @DELETE
  @Path("/{id}/opponents")
  public JsonObject removeOpponent(@PathParam("id") Long groupId, @QueryParam("opponentId") Long opponentId) {
    return null;
  }

  @GET
  @Path("/{id}/assignableOpponents")
  public JsonArray getAssignableOpponents(@PathParam("id") Long groupId){
    JsonArray result = null;

    Set<Opponent> opponents = gRepository.getAssignableOpponents(groupId);

    // all opponents must have same type (see opponentTypeRestriction)
    if(opponents.isEmpty()){
      return emptyArray();
    }
    else{ ModelJsonConverter oConverter = opponentJsonConverterProvider.getConverter(opponents);
      result = oConverter.toJsonArray(opponents);
    }

    return result;
  }
}
