package org.cc.torganizer.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.cc.torganizer.core.entities.*;
import org.cc.torganizer.rest.json.*;

import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.SQUAD;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;

@Stateless
@Path("/tournaments")
@Produces("application/json")
@Consumes("application/json")
public class TournamentsResource extends AbstractResource {

  private static final String TOURNAMENT_FIND_BY_ID_QUERY_NAME = "Tournament.findById";

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @Inject
  private TournamentJsonConverter tConverter;

  @Inject
  private OpponentJsonConverterProvider ocProvider;

  @Inject
  private DisciplineJsonConverter dConverter;

  @POST
  public JsonObject create(JsonObject jsonObject) {

    Tournament tournament = tConverter.toModel(jsonObject);
    // client can send '0' with a detached object exception as the result
    tournament.setId(null);

    entityManager.persist(tournament);
    entityManager.flush();

    return tConverter.toJsonObject(tournament);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME, Tournament.class);
    namedQuery.setParameter("id", id);
    Tournament tournament = namedQuery.getSingleResult();

    return tConverter.toJsonObject(tournament);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {

    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Tournament> namedQuery = entityManager.createNamedQuery("Tournament.findAll", Tournament.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    List<Tournament> tournaments = namedQuery.getResultList();

    return tConverter.toJsonArray(tournaments);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Tournament tournament = tConverter.toModel(jsonObject);
    entityManager.merge(tournament);

    return tConverter.toJsonObject(tournament);
  }

  @DELETE
  public JsonObject delete(JsonObject jsonObject) {
    Tournament tournament = tConverter.toModel(jsonObject);
    entityManager.remove(tournament);

    return tConverter.toJsonObject(tournament);
  }

  @GET
  @Path("/{id}/players")
  public JsonArray players(@PathParam("id") Long tournamentId, @QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Tournament.findPlayers", Player.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);

    List<Player> players = namedQuery.getResultList();

    PlayerJsonConverter pConverter = (PlayerJsonConverter) ocProvider.getConverter(PLAYER);
    return pConverter.toJsonArray(players);
  }

  /**
   * Getting all opponents, which can be assigned to a given discipline for the tournament.
   */
  @GET
  @Path("/{id}/assignable-opponents")
  public JsonArray getAssignableOpponents(@PathParam("id") Long tournamentId, @QueryParam("disciplineId") Integer disciplineId) {
    // load discipline with restrictions
    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery("Discipline.findById", Discipline.class);
    namedQuery.setParameter("id", disciplineId);
    Discipline discipline = namedQuery.getSingleResult();
    Set<Opponent> disciplineOpponents = discipline.getOpponents();
    OpponentTypeRestriction otRestriction = (OpponentTypeRestriction) discipline.getRestriction(OPPONENT_TYPE_RESTRICTION);

    // load tournaments opponents
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME, Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    Tournament tournament = namedTournamentQuery.getSingleResult();
    Set<Opponent> opponents = tournament.getOpponents();

    // filter opponents
    List<Opponent> assignableOpponents = opponents
      .stream()
      .filter(opponent -> discipline.isAssignable(opponent))
      .collect(Collectors.toList());

    OpponentType opponentType = otRestriction.getOpponentType();
    ModelJsonConverter converter = ocProvider.getConverter(opponentType);

    return converter.toJsonArray(assignableOpponents);
  }

  @POST
  @Path("/{tid}/players")
  public JsonObject addPlayer(@PathParam("tid") Long tournamentId, @QueryParam("pid") Long playerId) {
    // load player
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", playerId);
    Player player = namedQuery.getSingleResult();

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME,
      Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    Tournament tournament = namedTournamentQuery.getSingleResult();

    // persist tournament
    tournament.getOpponents().add(player);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    PlayerJsonConverter pConverter = (PlayerJsonConverter) ocProvider.getConverter(PLAYER);
    return pConverter.toJsonObject(player);
  }

  /**
   * Remove player from tournament. The player is not deleted at all.
   */
  @DELETE
  @Path("/{tid}/players/{pid}")
  public JsonObject removePlayer(@PathParam("tid") Long tournamentId, @PathParam("pid") Long playerId) {
    // load player
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", playerId);
    Player player = namedQuery.getSingleResult();

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME,
      Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    Tournament tournament = namedTournamentQuery.getSingleResult();

    // persist tournament
    tournament.getOpponents().remove(player);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    PlayerJsonConverter pConverter = (PlayerJsonConverter) ocProvider.getConverter(PLAYER);
    return pConverter.toJsonObject(player);
  }

  @GET
  @Path("/{id}/players/count")
  public Long playersCount(@PathParam("id") Long tournamentId) {
    Query query = entityManager.createNamedQuery("Tournament.countPlayers");
    query.setParameter("id", tournamentId);

    return (long) query.getSingleResult();
  }

  @GET
  @Path("/{id}/squads")
  public JsonArray squads(@PathParam("id") Long tournamentId, @QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Tournament.findSquads", Squad.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    List<Squad> squads = namedQuery.getResultList();

    SquadJsonConverter sConverter = (SquadJsonConverter) ocProvider.getConverter(SQUAD);
    return sConverter.toJsonArray(squads);
  }

  @POST
  @Path("/{id}/squads")
  public JsonObject squads(@PathParam("id") Long tournamentId, @QueryParam("sid") Long squadId) {
    // load player
    TypedQuery<Squad> namedQuery = entityManager.createNamedQuery("Opponent.findById", Squad.class);
    namedQuery.setParameter("id", squadId);
    Squad squad = namedQuery.getSingleResult();

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME,
      Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    Tournament tournament = namedTournamentQuery.getSingleResult();

    // persist tournament
    tournament.getOpponents().add(squad);
    entityManager.persist(tournament);

    SquadJsonConverter sConverter = (SquadJsonConverter) ocProvider.getConverter(SQUAD);
    return sConverter.toJsonObject(squad);
  }

  @GET
  @Path("/{id}/squads/count")
  public Long squadsCount(@PathParam("id") Long tournamentId) {
    Query query = entityManager.createNamedQuery("Tournament.countSquads");
    query.setParameter("id", tournamentId);

    return (long) query.getSingleResult();
  }

  @GET
  @Path("/{id}/disciplines")
  public JsonArray readDisciplines(@PathParam("id") Long tournamentId) {
    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery("Tournament.findDisciplines", Discipline.class);
    namedQuery.setParameter("id", tournamentId);
    List<Discipline> disciplines = namedQuery.getResultList();

    return dConverter.toJsonArray(disciplines);
  }

  @POST
  @Path("/{id}/disciplines")
  public JsonObject addDiscipline(@PathParam("id") Long tournamentId, @QueryParam("did") Long disciplineId) {
    // load discipline
    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery("Discipline.findById", Discipline.class);
    namedQuery.setParameter("id", disciplineId);
    List<Discipline> disciplines = namedQuery.getResultList();
    Discipline disciplineToAdd = disciplines.get(0);

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME,
      Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    List<Tournament> tournaments = namedTournamentQuery.getResultList();
    Tournament tournament = tournaments.get(0);

    // persist tournament
    tournament.getDisciplines().add(disciplineToAdd);
    entityManager.persist(tournament);

    return dConverter.toJsonObject(disciplineToAdd);
  }
}
