package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.persistence.RoundsRepository;
import org.cc.torganizer.rest.json.ModelJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;
import org.cc.torganizer.rest.json.RoundJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import java.util.List;
import java.util.Set;

import static org.cc.torganizer.rest.json.ModelJsonConverter.emptyArray;

@Stateless
@Path("/rounds")
@Produces("application/json")
public class RoundsResource extends AbstractResource {

  @Inject
  private RoundsRepository rRepository;

  @Inject
  private RoundJsonConverter rConverter;

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    Round round = rConverter.toModel(jsonObject, new Round());
    round = rRepository.create(round);

    return rConverter.toJsonObject(round);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long roundId) {
    Round round = rRepository.read(roundId);

    return rConverter.toJsonObject(round);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Round> roounds = rRepository.read(offset, maxResults);
    return rConverter.toJsonArray(roounds);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Round round = rRepository.read(id);

    round = rConverter.toModel(jsonObject, round);
    rRepository.update(round);

    return  rConverter.toJsonObject(round);
  }

  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Round round = rRepository.read(id);
    round = rRepository.delete(round);

    return rConverter.toJsonObject(round);
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

  @SuppressWarnings("unchecked")
  @GET
  @Path("/{id}/opponents-assignable-to-group")
  public JsonArray getOpponentsAssignableToGroup(@PathParam("id") Long disciplineId){
    JsonArray result = null;

    Set<Opponent> opponents = rRepository.getOpponentsAssignableToGroup(disciplineId);

    // all opponents must have same type (see opponentTypeRestriction)
    if(opponents.isEmpty()){
      result = emptyArray();
    }
    else{
      ModelJsonConverter<Opponent> oConverter = opponentJsonConverterProvider.getConverter(opponents);
      result = oConverter.toJsonArray(opponents);
    }

    return result;
  }
}
