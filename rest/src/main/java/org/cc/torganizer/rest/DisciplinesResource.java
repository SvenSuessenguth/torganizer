package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.rest.json.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import java.util.List;

import static org.cc.torganizer.rest.json.ModelJsonConverter.emptyArray;

@Stateless
@Path("/disciplines")
@Produces("application/json")
public class DisciplinesResource extends AbstractResource {

  @Inject
  private DisciplinesRepository disciplineRepo;

  @Inject
  private DisciplineJsonConverter disicplineConverter;

  @Inject
  private RestrictionJsonConverter restrictionConverter;

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
    Discipline discipline = disicplineConverter.toModel(jsonObject, new Discipline());

    JsonArray jsonArray = jsonObject.getJsonArray("restrictions");
    for(int i=0; i<jsonArray.size(); i++){
      JsonObject r = jsonArray.getJsonObject(i);
      Restriction restriction = Restriction.Discriminator.byId(r.getString("discriminator")).create();

      restriction = restrictionConverter.toModel(r, restriction);
      discipline.addRestriction(restriction);
    }

    discipline = disciplineRepo.create(discipline);

    return disicplineConverter.toJsonObject(discipline);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long disciplineId) {
    Discipline discipline = disciplineRepo.read(disciplineId);

    return disicplineConverter.toJsonObject(discipline);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Discipline> disciplines = disciplineRepo.read(offset, maxResults);

    return disicplineConverter.toJsonArray(disciplines);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Discipline discipline = disciplineRepo.read(id);

    discipline = disicplineConverter.toModel(jsonObject, discipline);
    restrictionConverter.toModels(jsonObject.getJsonArray("restrictions"), discipline.getRestrictions());

    discipline = disciplineRepo.update(discipline);

    return disicplineConverter.toJsonObject(discipline);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Opponents
  //
  //--------------------------------------------------------------------------------------------------------------------
  @SuppressWarnings("unchecked")
  @GET
  @Path("/{id}/opponents")
  public JsonArray getOpponents(@PathParam("id") Long disciplineId, @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Opponent> opponents = disciplineRepo.getOpponents(disciplineId, offset, maxResults);

    JsonArray result;

    // all opponents must have same type (see opponentTypeRestriction)
    if(opponents.isEmpty()){
      return emptyArray();
    }
    else{
      ModelJsonConverter<Opponent> oConverter = opponentJsonConverterProvider.getConverter(opponents);
      result = oConverter.toJsonArray(opponents);
    }

    return result;
  }

  @POST
  @Path("/{id}/opponents")
  public JsonObject addOpponent(@PathParam("id") Long disciplineId, @QueryParam("opponentId") Long opponentId) {
    Discipline discipline = disciplineRepo.addOpponent(disciplineId, opponentId);

    return disicplineConverter.toJsonObject(discipline);
  }

  @DELETE
  @Path("/{id}/opponents")
  public JsonObject removeOpponent(@PathParam("id") Long disciplineId, @QueryParam("opponentId") Long opponentId) {
    Discipline discipline = disciplineRepo.removeOpponent(disciplineId, opponentId);

    return disicplineConverter.toJsonObject(discipline);
  }

  //--------------------------------------------------------------------------------------------------------------------
  //
  // Rounds
  //
  //--------------------------------------------------------------------------------------------------------------------
  @GET
  @Path("/{id}/rounds")
  public JsonArray getRounds(@PathParam("id") Long disciplineId, @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Round> rounds = disciplineRepo.getRounds(disciplineId, offset, maxResults);
    return roundConverter.toJsonArray(rounds);
  }

  @POST
  @Path("/{id}/rounds")
  public JsonObject addRound(@PathParam("id") Long disciplineId, @QueryParam("roundId") Long roundId) {
    Discipline discipline = disciplineRepo.addRound(disciplineId, roundId);
    return disicplineConverter.toJsonObject(discipline);
  }

  @DELETE
  @Path("/{id}/rounds")
  public JsonObject removeRound(@PathParam("id") Long disciplineId, @QueryParam("roundId") Long roundId) {
    Discipline discipline = disciplineRepo.removeRound(disciplineId, roundId);
    return disicplineConverter.toJsonObject(discipline);
  }
}
