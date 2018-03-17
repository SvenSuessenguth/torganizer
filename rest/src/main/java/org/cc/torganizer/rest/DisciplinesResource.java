package org.cc.torganizer.rest;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Restriction;
import static org.cc.torganizer.rest.AbstractResource.DEFAULT_OFFSET;
import org.cc.torganizer.rest.json.DisciplineJsonConverter;

@Stateless
@Path("/disciplines")
@Produces("application/json")
public class DisciplinesResource extends AbstractResource {

  private static final String DISCIPLINE_FIND_BY_ID_QUERY_NAME = "Discipline.findById";
  private static final String DISCIPLINE_FIND_ALL_QUERY_NAME = "Discipline.findAll";

  @Inject
  private DisciplineJsonConverter converter;

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @POST
  public JsonObject create(JsonObject jsonObject) {

    Discipline discipline = converter.toModel(jsonObject);

    // client can send '0' with a detached object exception as the result
    discipline.setId(null);
    discipline.getRestrictions().forEach((Restriction restriction) -> restriction.setId(null));

    entityManager.persist(discipline);
    entityManager.flush();

    return converter.toJsonObject(discipline);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {

    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_ALL_QUERY_NAME, Discipline.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    List<Discipline> disciplines = namedQuery.getResultList();

    return converter.toJsonArray(disciplines);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {

    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_BY_ID_QUERY_NAME, Discipline.class);
    namedQuery.setParameter("id", id);
    Discipline discipline = namedQuery.getSingleResult();

    return converter.toJsonObject(discipline);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {

    Discipline discipline = converter.toModel(jsonObject);
    discipline = entityManager.merge(discipline);

    return converter.toJsonObject(discipline);
  }

  @GET
  @Path("/{id}/opponents")
  public JsonObject opponents(@PathParam("id") Long id) {
    TypedQuery<Opponent> namedQuery = entityManager.createNamedQuery("Discipline.findOpponents", Opponent.class);
    namedQuery.setParameter("id", id);
    List<Opponent> opponents = namedQuery.getResultList();

    return null;
  }

  @GET
  @Path("/{id}/opponents/add")
  public JsonObject opponents(@PathParam("id") Long disciplineId, @QueryParam("opponentId") Long opponentId) {
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

    return null;
  }
}
