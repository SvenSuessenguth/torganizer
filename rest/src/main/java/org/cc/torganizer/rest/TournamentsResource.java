package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.*;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.persistence.TournamentsRepository;
import org.cc.torganizer.rest.json.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.validation.*;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.SQUAD;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;

@Stateless
@Path("/tournaments")
@Produces("application/json")
@Consumes("application/json")
public class TournamentsResource extends AbstractResource {
  @Inject
  private TournamentsRepository tRepository;
  @Inject
  private DisciplinesRepository dRepository;

  @Inject
  private TournamentJsonConverter tConverter;

  @Inject
  private DisciplineJsonConverter dConverter;

  @Inject
  private OpponentJsonConverterProvider ocProvider;


  @POST
  public Response create(JsonObject jsonObject) {
    Tournament tournament = tConverter.toModel(jsonObject, new Tournament());
    // client can send '0' with a detached object exception as the result
    tournament.setId(null);

    validate(tournament);

    Tournament savedTournament = tRepository.create(tournament);
    return Response.ok(tConverter.toJsonObject(savedTournament)).build();
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {
    Tournament tournament = tRepository.read(id);
    return tConverter.toJsonObject(tournament);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Tournament> tournaments = tRepository.getTournaments(offset, maxResults);

    return tConverter.toJsonArray(tournaments);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Tournament tournament = tRepository.read(id);

    tournament = tConverter.toModel(jsonObject, tournament);
    validate(tournament);

    tRepository.update(tournament);

    return tConverter.toJsonObject(tournament);
  }

  @DELETE
  public JsonObject delete(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Tournament tournament = tRepository.read(id);

    tRepository.delete(tournament);

    return tConverter.toJsonObject(tournament);
  }

  @GET
  @Path("/{id}/players")
  public JsonArray players(@PathParam("id") Long tournamentId, @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Player> players = tRepository.getPlayersOrderedByLastName(tournamentId, offset, maxResults);

    PlayerJsonConverter pConverter = (PlayerJsonConverter) ocProvider.getConverter(PLAYER);
    return pConverter.toJsonArray(players);
  }

  /**
   * Getting all opponents, which can be assigned to a given discipline for the tournament.
   * @param tournamentId ID of the tournament
   * @param disciplineId ID of the discipline
   * @param offset offset
   * @param maxResults max results
   * @return JasonArray with the assignable opponents
   */
  @GET
  @Path("/{id}/assignable-opponents")
  public JsonArray getAssignableOpponents(@PathParam("id") Long tournamentId, @QueryParam("disciplineId") Long disciplineId,
                                          @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    Discipline discipline = dRepository.read(disciplineId);
    List<Opponent> opponents = tRepository.getOpponentsForDiscipline(tournamentId, discipline, offset, maxResults);

    OpponentTypeRestriction otRestriction = (OpponentTypeRestriction) discipline.getRestriction(OPPONENT_TYPE_RESTRICTION);
    OpponentType opponentType = otRestriction.getOpponentType();
    ModelJsonConverter<Opponent> converter = ocProvider.getConverter(opponentType);

    return converter.toJsonArray(opponents);
  }

  @POST
  @Path("/{tid}/players")
  public JsonObject addPlayer(@PathParam("tid") Long tournamentId, @QueryParam("pid") Long playerId) {
    Player player = tRepository.addPlayer(tournamentId, playerId);
    PlayerJsonConverter pConverter = (PlayerJsonConverter) ocProvider.getConverter(PLAYER);

    return pConverter.toJsonObject(player);
  }

  /**
   * Remove player from tournament. The player is not deleted at all.
   * @param tournamentId ID of the tournament
   * @param playerId ID of the player to remove
   * @return JsonObject of the removed player
   */
  @DELETE
  @Path("/{tid}/players/{pid}")
  public JsonObject removePlayer(@PathParam("tid") Long tournamentId, @PathParam("pid") Long playerId) {
    Player player = tRepository.removePlayer(tournamentId, playerId);
    PlayerJsonConverter pConverter = (PlayerJsonConverter) ocProvider.getConverter(PLAYER);

    return pConverter.toJsonObject(player);
  }

  @GET
  @Path("/{id}/players/count")
  public Long playersCount(@PathParam("id") Long tournamentId) {
    return tRepository.countPlayers(tournamentId);
  }

  @GET
  @Path("/{id}/squads")
  public JsonArray squads(@PathParam("id") Long tournamentId, @QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Squad> squads = tRepository.getSquads(tournamentId, offset, maxResults);
    SquadJsonConverter sConverter = (SquadJsonConverter) ocProvider.getConverter(SQUAD);

    return sConverter.toJsonArray(squads);
  }

  @POST
  @Path("/{id}/squads")
  public JsonObject squads(@PathParam("id") Long tournamentId, @QueryParam("sid") Long squadId) {
    Squad squad = tRepository.addSquad(tournamentId, squadId);
    SquadJsonConverter sConverter = (SquadJsonConverter) ocProvider.getConverter(SQUAD);
    
    return sConverter.toJsonObject(squad);
  }

  @GET
  @Path("/{id}/squads/count")
  public Long squadsCount(@PathParam("id") Long tournamentId) {
    return tRepository.countSquads(tournamentId);
  }

  @GET
  @Path("/{id}/disciplines")
  public JsonArray readDisciplines(@PathParam("id") Long tournamentId) {
    List<Discipline> disciplines = tRepository.getDisciplines(tournamentId, null, null);

    return dConverter.toJsonArray(disciplines);
  }

  @POST
  @Path("/{id}/disciplines")
  public JsonObject addDiscipline(@PathParam("id") Long tournamentId, @QueryParam("did") Long disciplineId) {
    Discipline discipline = dRepository.read(disciplineId);
    tRepository.addDiscipline(tournamentId, discipline);

    return dConverter.toJsonObject(discipline);
  }

  private void validate(Tournament tournament) {
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<Tournament>> violations = validator.validate( tournament );
      if(!violations.isEmpty()){
          throw new ConstraintViolationException(violations);
      }
  }
}
