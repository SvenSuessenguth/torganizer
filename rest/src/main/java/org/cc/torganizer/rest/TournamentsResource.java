package org.cc.torganizer.rest;

import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.SQUAD;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;

import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.persistence.TournamentsRepository;
import org.cc.torganizer.rest.json.BaseModelJsonConverter;
import org.cc.torganizer.rest.json.DisciplineJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;
import org.cc.torganizer.rest.json.PlayerJsonConverter;
import org.cc.torganizer.rest.json.SquadJsonConverter;
import org.cc.torganizer.rest.json.TournamentJsonConverter;

@Stateless
@Path("/tournaments")
@Produces("application/json")
@Consumes("application/json")
public class TournamentsResource extends AbstractResource {
  @Inject
  private TournamentsRepository tournamentsRepo;
  @Inject
  private DisciplinesRepository disciplineRepo;

  @Inject
  private TournamentJsonConverter tournamentConverter;

  @Inject
  private DisciplineJsonConverter disciplineConverter;

  @Inject
  private OpponentJsonConverterProvider opponentConverterProvider;

  /**
   * Create, validate and persist an new {@link org.cc.torganizer.core.entities.Tournament}.
   */
  @POST
  public Response create(JsonObject jsonObject) {
    Tournament tournament = tournamentConverter.toModel(jsonObject, new Tournament());
    // client can send '0' with a detached object exception as the result
    tournament.setId(null);

    validate(tournament);

    Tournament savedTournament = tournamentsRepo.create(tournament);
    return Response.ok(tournamentConverter.toJsonObject(savedTournament)).build();
  }

  /**
   * Getting the tournament with the given tournamentId.
   */
  @GET
  @Path("{tournamentId}")
  public JsonObject readSingle(@PathParam("tournamentId") Long tournamentId) {
    Tournament tournament = tournamentsRepo.read(tournamentId);
    return tournamentConverter.toJsonObject(tournament);
  }

  /**
   * Getting multiple tournaments from offset to maxResults.
   */
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    List<Tournament> tournaments = tournamentsRepo.read(offset, maxResults);

    return tournamentConverter.toJsonArray(tournaments);
  }

  /**
   * Update the tournament with data from json.
   */
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Tournament tournament = tournamentsRepo.read(id);

    tournament = tournamentConverter.toModel(jsonObject, tournament);
    validate(tournament);

    tournamentsRepo.update(tournament);

    return tournamentConverter.toJsonObject(tournament);
  }

  /**
   * Deleting the tournament.
   */
  @DELETE
  public JsonObject delete(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Tournament tournament = tournamentsRepo.read(id);

    tournamentsRepo.delete(tournament);

    return tournamentConverter.toJsonObject(tournament);
  }

  /**
   * Getting the players of the tournament with the given id from offset to maxResults.
   */
  @GET
  @Path("/{id}/players")
  public JsonArray players(@PathParam("id") Long tournamentId, @QueryParam("offset") Integer offset,
                           @QueryParam("maxResults") Integer maxResults) {
    List<Player> players = tournamentsRepo.getPlayersOrderedByLastName(tournamentId, offset,
        maxResults);

    PlayerJsonConverter playerConverter =
        (PlayerJsonConverter) opponentConverterProvider.getConverter(PLAYER);

    return playerConverter.toJsonArray(players);
  }

  /**
   * Getting all opponents, which can be assigned to a given discipline for the tournament.
   *
   * @param tournamentId ID of the tournament
   * @param disciplineId ID of the discipline
   * @param offset       offset
   * @param maxResults   max results
   * @return JasonArray with the assignable opponents
   */
  @SuppressWarnings("unchecked")
  @GET
  @Path("/{id}/assignable-opponents")
  public JsonArray getAssignableOpponents(@PathParam("id") Long tournamentId,
                                          @QueryParam("disciplineId") Long disciplineId,
                                          @QueryParam("offset") Integer offset,
                                          @QueryParam("maxResults") Integer maxResults) {

    Discipline discipline = disciplineRepo.read(disciplineId);
    List<Opponent> opponents = tournamentsRepo.getAssignableOpponentsForDiscipline(tournamentId,
        discipline, offset, maxResults);

    OpponentTypeRestriction otRestriction =
        (OpponentTypeRestriction) discipline.getRestriction(OPPONENT_TYPE_RESTRICTION);
    OpponentType oppType = otRestriction.getOpponentType();
    BaseModelJsonConverter<Opponent> converter = opponentConverterProvider.getConverter(oppType);

    return converter.toJsonArray(opponents);
  }

  /**
   * Add a player with the given id to the tournament with the given id.
   */
  @POST
  @Path("/{tid}/players")
  public JsonObject addPlayer(@PathParam("tid") Long tournamentId,
                              @QueryParam("pid") Long playerId) {
    Player player = tournamentsRepo.addPlayer(tournamentId, playerId);
    PlayerJsonConverter playerConverter =
        (PlayerJsonConverter) opponentConverterProvider.getConverter(PLAYER);

    return playerConverter.toJsonObject(player);
  }

  /**
   * Remove player from tournament. The player is not deleted at all.
   *
   * @param tournamentId ID of the tournament
   * @param playerId     ID of the player to remove
   * @return JsonObject of the removed player
   */
  @DELETE
  @Path("/{tid}/players/{pid}")
  public JsonObject removePlayer(@PathParam("tid") Long tournamentId,
                                 @PathParam("pid") Long playerId) {

    Player player = tournamentsRepo.removePlayer(tournamentId, playerId);
    PlayerJsonConverter playerConverter =
        (PlayerJsonConverter) opponentConverterProvider.getConverter(PLAYER);

    return playerConverter.toJsonObject(player);
  }

  @GET
  @Path("/{id}/players/count")
  public Long playersCount(@PathParam("id") Long tournamentId) {
    return tournamentsRepo.countPlayers(tournamentId);
  }

  /**
   * Getting the squads from the tournament with the given id frm offset to maxResults.
   */
  @GET
  @Path("/{id}/squads")
  public JsonArray squads(@PathParam("id") Long tournamentId, @QueryParam("offset") Integer offset,
                          @QueryParam("maxResults") Integer maxResults) {

    List<Squad> squads = tournamentsRepo.getSquads(tournamentId, offset, maxResults);
    SquadJsonConverter squadConverter =
        (SquadJsonConverter) opponentConverterProvider.getConverter(SQUAD);

    return squadConverter.toJsonArray(squads);
  }

  /**
   * Add a squad with the given id to the tournament with the given id.
   */
  @POST
  @Path("/{id}/squads")
  public JsonObject addSquad(@PathParam("id") Long tournamentId, @QueryParam("sid") Long squadId) {
    Squad squad = tournamentsRepo.addSquad(tournamentId, squadId);
    SquadJsonConverter squadConverter =
        (SquadJsonConverter) opponentConverterProvider.getConverter(SQUAD);

    return squadConverter.toJsonObject(squad);
  }

  @GET
  @Path("/{id}/squads/count")
  public Long squadsCount(@PathParam("id") Long tournamentId) {
    return tournamentsRepo.countSquads(tournamentId);
  }

  /**
   * Getting disciplines from the tournament with the given id with defaults for offset
   * to maxResults.
   */
  @GET
  @Path("/{id}/disciplines")
  public JsonArray readDisciplines(@PathParam("id") Long tournamentId) {
    List<Discipline> disciplines = tournamentsRepo.getDisciplines(tournamentId, null, null);

    return disciplineConverter.toJsonArray(disciplines);
  }

  /**
   * Add the discipline with the given id to the tournament with the given id.
   */
  @POST
  @Path("/{id}/disciplines")
  public JsonObject addDiscipline(@PathParam("id") Long tournamentId,
                                  @QueryParam("did") Long disciplineId) {

    Discipline discipline = disciplineRepo.read(disciplineId);
    tournamentsRepo.addDiscipline(tournamentId, discipline);

    return disciplineConverter.toJsonObject(discipline);
  }

  private void validate(Tournament tournament) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<Tournament>> violations = validator.validate(tournament);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
