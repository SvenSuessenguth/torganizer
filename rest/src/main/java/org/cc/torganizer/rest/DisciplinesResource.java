package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.rest.json.DisciplineJsonConverter;
import org.cc.torganizer.rest.json.ModelJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;
import org.cc.torganizer.rest.json.RestrictionJsonConverter;

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
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

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
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    List<Discipline> disciplines = dRepository.read(offset, length);

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
}
