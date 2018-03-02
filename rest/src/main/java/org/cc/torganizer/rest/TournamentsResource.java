package org.cc.torganizer.rest;

import java.util.List;
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
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.rest.container.DisciplinesContainer;
import org.cc.torganizer.rest.json.PlayerJsonConverter;
import org.cc.torganizer.rest.json.SquadJsonConverter;
import org.cc.torganizer.rest.json.TournamentJsonConverter;

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
  private PlayerJsonConverter pConverter;
  
  @Inject
  private SquadJsonConverter sConverter;

  @POST
  public JsonObject create(JsonObject jsonObject) {

    Tournament tournament = tConverter.toModel(jsonObject);
    // vom client kann die id '0' geliefert werden, sodass eine detached-entity-Exception geworfen wird.
    tournament.setId(null);

    entityManager.persist(tournament);
    entityManager.flush();
    
    final JsonObject result = tConverter.toJsonObject(tournament);
    
    return result;
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
  public JsonObject delete(JsonObject jsonObject){
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

    return pConverter.toJsonArray(players);
  }

  @POST
  @Path("/{tid}/players")
  public JsonObject players(@PathParam("tid") Long tournamentId, @QueryParam("pid") Long playerId) {
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

    return pConverter.toJsonObject(player);
  }
  
  /**
   * Remove player from tournament. The player is not deleted at all.
   * @param tournamentId
   * @param playerId
   * @return 
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
  public DisciplinesContainer disciplines(@PathParam("id") Long tournamentId) {
    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery("Tournament.findDisciplines", Discipline.class);
    namedQuery.setParameter("id", tournamentId);
    List<Discipline> disciplines = namedQuery.getResultList();

    return new DisciplinesContainer(disciplines);
  }

  @POST
  @Path("/{id}/disciplines")
  public Discipline disciplines(@PathParam("id") Long tournamentId, @QueryParam("disciplineId") Long disciplineId) {
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

    return disciplineToAdd;
  }
}