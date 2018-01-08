package org.cc.torganizer.rest;

import java.net.URI;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.created;
import javax.ws.rs.core.UriInfo;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.rest.container.DisciplinesContainer;
import org.cc.torganizer.rest.container.OpponentsContainer;
import org.cc.torganizer.rest.json.PlayerJsonConverter;
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

  @POST
  public Response create(JsonObject jsonObject, @Context UriInfo uriInfo) {

    Tournament tournament = tConverter.toModel(jsonObject);
    // vom client kann die id '0' geliefert werden, sodass eine detached-entity-Exception geworfen wird.
    tournament.setId(null);

    entityManager.persist(tournament);
    entityManager.flush();
    
    final JsonObject result = tConverter.toJsonObject(tournament);
    URI uri = uriInfo.getAbsolutePathBuilder().path(""+tournament.getId()).build();
    
    return created(uri).entity(result).build();
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
  @Path("/{id}/subscribers")
  public JsonArray subscribers(@PathParam("id") Long tournamentId, @QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }
            
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Tournament.findSubscribers", Player.class);
    namedQuery.setParameter("id", tournamentId);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    
    List<Player> players = namedQuery.getResultList();

    return pConverter.toJsonArray(players);
  }

  @POST
  @Path("/{tid}/subscribers")
  public JsonObject addSubscriber(@PathParam("tid") Long tournamentId, @QueryParam("pid") Long playerId) {
    // load player
    TypedQuery<Player> namedQuery = entityManager.createNamedQuery("Player.findById", Player.class);
    namedQuery.setParameter("id", playerId);
    List<Player> players = namedQuery.getResultList();
    Player playerToAdd = players.get(0);

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME,
        Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    List<Tournament> tournaments = namedTournamentQuery.getResultList();
    Tournament tournament = tournaments.get(0);

    // persist tournament
    tournament.getSubscribers().add(playerToAdd);
    entityManager.persist(tournament);
    // to get the id
    entityManager.flush();

    return pConverter.toJsonObject(playerToAdd);
  }
  
  @GET
  @Path("/{id}/subscribers/count")
  public Long subscribersCount(@PathParam("id") Long tournamentId) {
    Query query = entityManager.createNamedQuery("Tournament.countSubscribers");
    query.setParameter("id", tournamentId);
    
    return (long) query.getSingleResult();
  }
  
  @GET
  @Path("/{id}/opponents")
  public OpponentsContainer opponents(@PathParam("id") Long tournamentId) {
    TypedQuery<Opponent> namedQuery = entityManager.createNamedQuery("Tournament.findOpponents", Opponent.class);
    namedQuery.setParameter("id", tournamentId);
    List<Opponent> opponents = namedQuery.getResultList();

    return new OpponentsContainer(opponents);
  }

  @POST
  @Path("/{id}/opponents")
  public Opponent opponents(@PathParam("id") Long tournamentId, @QueryParam("opponentId") Long opponentId) {
    // load player
    TypedQuery<Opponent> namedQuery = entityManager.createNamedQuery("Opponent.findById", Opponent.class);
    namedQuery.setParameter("id", opponentId);
    List<Opponent> opponents = namedQuery.getResultList();
    Opponent opponentToAdd = opponents.get(0);

    // load tournament
    TypedQuery<Tournament> namedTournamentQuery = entityManager.createNamedQuery(TOURNAMENT_FIND_BY_ID_QUERY_NAME,
        Tournament.class);
    namedTournamentQuery.setParameter("id", tournamentId);
    List<Tournament> tournaments = namedTournamentQuery.getResultList();
    Tournament tournament = tournaments.get(0);

    // persist tournament
    tournament.getOpponents().add(opponentToAdd);
    entityManager.persist(tournament);

    return opponentToAdd;
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