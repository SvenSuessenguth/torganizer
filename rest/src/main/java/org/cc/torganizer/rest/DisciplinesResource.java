package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.rest.json.DisciplineJsonConverter;
import org.cc.torganizer.rest.json.ModelJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import java.util.List;
import java.util.Set;

@Stateless
@Path("/disciplines")
@Produces("application/json")
public class DisciplinesResource extends AbstractResource {

  private static final String DISCIPLINE_FIND_BY_ID_QUERY_NAME = "Discipline.findById";
  private static final String DISCIPLINE_FIND_ALL_QUERY_NAME = "Discipline.findAll";

  private DisciplinesRepository dRepository;

  @Inject
  private DisciplineJsonConverter converter;

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    Discipline discipline = converter.toModel(jsonObject);
    discipline = dRepository.create(discipline);

    return converter.toJsonObject(discipline);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long disciplineId) {
    Discipline discipline = dRepository.read(disciplineId);

    return converter.toJsonObject(discipline);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    List<Discipline> disciplines = dRepository.read(offset, length);

    return converter.toJsonArray(disciplines);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Discipline discipline = converter.toModel(jsonObject);
    discipline = dRepository.update(discipline);

    return converter.toJsonObject(discipline);
  }

  @GET
  @Path("/{id}/opponents")
  public JsonArray getOpponents(@PathParam("id") Long disciplineId) {
    List<Opponent> opponents = dRepository.getOpponents(disciplineId, null, null);

    JsonArray result = null;

    // all opponents must have same type
    OpponentType opponentType = null;
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
    // load Opponent
    TypedQuery<Opponent> namedQuery = entityManager.createNamedQuery("Opponent.findById", Opponent.class);
    namedQuery.setParameter("id", opponentId);
    List<Opponent> opponents = namedQuery.getResultList();
    Opponent opponentToAdd = opponents.get(0);

    // load discipline
    TypedQuery<Discipline> namedDisciplineQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_BY_ID_QUERY_NAME,
      Discipline.class);
    namedDisciplineQuery.setParameter("id", disciplineId);
    List<Discipline> disciplines = namedDisciplineQuery.getResultList();
    Discipline discipline = disciplines.get(0);

    // persist discipline
    discipline.getOpponents().add(opponentToAdd);
    entityManager.persist(discipline);

    return converter.toJsonObject(discipline);
  }
}
