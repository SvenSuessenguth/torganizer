package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.*;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.rest.json.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import java.util.List;

@Stateless
@Path("/disciplines")
@Produces("application/json")
public class DisciplinesResource extends AbstractResource {

  @Inject
  private DisciplinesRepository dRepository;

  @Inject
  private DisciplineJsonConverter dConverter;

  @Inject
  private RestrictionJsonConverter rConverter;

  @Inject
  private RoundJsonConverter roundConverter;

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  //--------------------------------------------------------------------------------------------------------------------
  //
  // CRUD
  //
  //--------------------------------------------------------------------------------------------------------------------

  @POST
  public JsonObject create(JsonObject jsonObject) {
    Discipline discipline = dConverter.toModel(jsonObject, new Discipline());

    JsonArray jsonArray = jsonObject.getJsonArray("restrictions");
    for(int i=0; i<jsonArray.size(); i++){
      JsonObject r = jsonArray.getJsonObject(i);
      Restriction restriction = Restriction.Discriminator.byId(r.getString("discriminator")).create();

      restriction = rConverter.toModel(r, restriction);
      discipline.addRestriction(restriction);
    }

    discipline = dRepository.create(discipline);

    return dConverter.toJsonObject(discipline);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long disciplineId) {
    Discipline discipline = dRepository.read(disciplineId);

    return dConverter.toJsonObject(discipline);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Discipline> disciplines = dRepository.read(offset, maxResults);

    return dConverter.toJsonArray(disciplines);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Discipline discipline = dRepository.read(id);

    discipline = dConverter.toModel(jsonObject, discipline);
    rConverter.toModels(jsonObject.getJsonArray("restrictions"), discipline.getRestrictions());

    discipline = dRepository.update(discipline);

    return dConverter.toJsonObject(discipline);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Opponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  @GET
  @Path("/{id}/opponents")
  public JsonArray getOpponents(@PathParam("id") Long disciplineId, @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Opponent> opponents = dRepository.getOpponents(disciplineId, offset, maxResults);

    JsonArray result;

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

  @POST
  @Path("/{id}/opponents")
  public JsonObject addOpponent(@PathParam("id") Long disciplineId, @QueryParam("opponentId") Long opponentId) {
    Discipline discipline = dRepository.addOpponent(disciplineId, opponentId);

    return dConverter.toJsonObject(discipline);
  }

  @DELETE
  @Path("/{id}/opponents")
  public JsonObject removeOpponent(@PathParam("id") Long disciplineId, @QueryParam("opponentId") Long opponentId) {
    Discipline discipline = dRepository.removeOpponent(disciplineId, opponentId);

    return dConverter.toJsonObject(discipline);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Rounds
  //
  //--------------------------------------------------------------------------------------------------------------------
  @GET
  @Path("/{id}/rounds")
  public JsonArray getRounds(@PathParam("id") Long disciplineId, @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Round> rounds = dRepository.getRounds(disciplineId, offset, maxResults);
    return roundConverter.toJsonArray(rounds);
  }

  @POST
  @Path("/{id}/round")
  public JsonObject addRound(@PathParam("id") Long disciplineId, @QueryParam("roundId") Long roundId) {
    Discipline discipline = dRepository.addRound(disciplineId, roundId);
    return dConverter.toJsonObject(discipline);
  }

  @DELETE
  @Path("/{id}/rounds")
  public JsonObject removeRound(@PathParam("id") Long disciplineId, @QueryParam("roundId") Long roundId) {
    Discipline discipline = dRepository.removeRound(disciplineId, roundId);
    return dConverter.toJsonObject(discipline);
  }
}
