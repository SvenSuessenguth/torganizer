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
import javax.ws.rs.core.Response;
import java.util.List;

import static org.cc.torganizer.rest.json.BaseModelJsonConverter.emptyArray;

@Stateless
@Path("/disciplines")
@Produces("application/json")
public class DisciplinesResource extends AbstractResource {

  @Inject
  private DisciplinesRepository disciplineRepo;

  @Inject
  private DisciplineJsonConverter disciplineConverter;

  @Inject
  private RestrictionJsonConverter restrictionConverter;

  @Inject
  private RoundJsonConverter roundConverter;

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  //-----------------------------------------------------------------------------------------------
  //
  // CRUD
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Create and persist a new discipline.
   */
  @POST
  public JsonObject create(JsonObject jsonObject) {
    Discipline discipline = disciplineConverter.toModel(jsonObject, new Discipline());

    JsonArray jsonArray = jsonObject.getJsonArray("restrictions");
    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject r = jsonArray.getJsonObject(i);
      Restriction restriction = Restriction.Discriminator.byId(r.getString("discriminator"))
        .create();

      restriction = restrictionConverter.toModel(r, restriction);
      discipline.addRestriction(restriction);
    }

    discipline = disciplineRepo.create(discipline);

    return disciplineConverter.toJsonObject(discipline);
  }

  /**
   * Getting the discipline with the given id.
   */
  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long disciplineId) {
    Discipline discipline = disciplineRepo.read(disciplineId);

    return disciplineConverter.toJsonObject(discipline);
  }

  /**
   * Reading multiple (offset to maxResults) disciplines.
   */
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {

    List<Discipline> disciplines = disciplineRepo.read(offset, maxResults);

    return disciplineConverter.toJsonArray(disciplines);
  }

  /**
   * Updating a discipline.
   */
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Discipline discipline = disciplineRepo.read(id);

    discipline = disciplineConverter.toModel(jsonObject, discipline);
    restrictionConverter.toModels(jsonObject.getJsonArray("restrictions"),
      discipline.getRestrictions());

    discipline = disciplineRepo.update(discipline);

    return disciplineConverter.toJsonObject(discipline);
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Opponents
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Getting the opponents (offset to maxResults) from the discipline with the given id.
   */
  @SuppressWarnings("unchecked")
  @GET
  @Path("/{id}/opponents")
  public JsonArray getOpponents(@PathParam("id") Long disciplineId,
                                @QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {

    List<Opponent> opponents = disciplineRepo.getOpponents(disciplineId, offset, maxResults);

    JsonArray result;

    // all opponents must have same type (see opponentTypeRestriction)
    if (opponents.isEmpty()) {
      return emptyArray();
    } else {
      BaseModelJsonConverter<Opponent> opponentConverter =
        opponentJsonConverterProvider.getConverter(opponents);
      result = opponentConverter.toJsonArray(opponents);
    }

    return result;
  }

  /**
   * Adding an opponent with the given id to the discipline with the given id.
   */
  @POST
  @Path("/{id}/opponents")
  public JsonObject addOpponent(@PathParam("id") Long disciplineId,
                                @QueryParam("opponentId") Long opponentId) {

    Discipline discipline = disciplineRepo.addOpponent(disciplineId, opponentId);

    return disciplineConverter.toJsonObject(discipline);
  }

  /**
   * Deleting an opponent with the given id from the discipline with the given id.
   */
  @DELETE
  @Path("/{id}/opponents")
  public JsonObject removeOpponent(@PathParam("id") Long disciplineId,
                                   @QueryParam("opponentId") Long opponentId) {

    Discipline discipline = disciplineRepo.removeOpponent(disciplineId, opponentId);
    return disciplineConverter.toJsonObject(discipline);
  }

  //-----------------------------------------------------------------------------------------------
  //
  // Rounds
  //
  //-----------------------------------------------------------------------------------------------

  /**
   * Get the rounds (offset to maxResults) from the discipline with the given id.
   */
  @GET
  @Path("/{id}/rounds")
  public JsonArray getRounds(@PathParam("id") Long disciplineId,
                             @QueryParam("offset") Integer offset,
                             @QueryParam("maxResults") Integer maxResults) {

    List<Round> rounds = disciplineRepo.getRounds(disciplineId, offset, maxResults);
    return roundConverter.toJsonArray(rounds);
  }

  /**
   * Getting the position of a round with given id from discipline with given id.
   */
  @GET
  @Path("/{id}/round-by-position/{position}")
  public Response getRoundByPosition(@PathParam("id") Long disciplineId,
                                     @PathParam("position") Integer position) {
    Round round = disciplineRepo.getRoundByPosition(disciplineId, position);

    if (round == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    JsonObject jsonObject = roundConverter.toJsonObject(round);

    return Response.ok().entity(jsonObject).build();
  }

  /**
   * Adding a round with the given id to the discipline with the given id.
   */
  @POST
  @Path("/{id}/rounds")
  public JsonObject addRound(@PathParam("id") Long disciplineId,
                             @QueryParam("roundId") Long roundId) {

    Discipline discipline = disciplineRepo.addRound(disciplineId, roundId);
    return disciplineConverter.toJsonObject(discipline);
  }

  /**
   * Deleting a round with the given id from the discipline with the given id.
   */
  @DELETE
  @Path("/{id}/rounds")
  public JsonObject removeRound(@PathParam("id") Long disciplineId,
                                @QueryParam("roundId") Long roundId) {

    Discipline discipline = disciplineRepo.removeRound(disciplineId, roundId);
    return disciplineConverter.toJsonObject(discipline);
  }
}
