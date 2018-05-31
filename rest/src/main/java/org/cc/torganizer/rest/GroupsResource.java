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
import java.util.List;
import java.util.Set;

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
    OpponentType opponentType;
    if(opponents.isEmpty()){
      JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
      result = arrayBuilder.build();
    }
    else{
      Opponent opponent = opponents.iterator().next();
      opponentType = opponent.getOpponentType();
      ModelJsonConverter oConverter = opponentJsonConverterProvider.getConverter(opponentType);
      result = oConverter.toJsonArray(opponents);
    }

    return result;
  }
}
