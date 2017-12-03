package org.cc.torganizer.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.rest.container.DisciplinesContainer;
import org.cc.torganizer.rest.container.OpponentsContainer;
import org.cc.torganizer.rest.container.RestrictionsContainer;

@Stateless
@Path("/disciplines")
@Produces("application/json")
public class DisciplinesResource {

  private static final String DISCIPLINE_FIND_BY_ID_QUERY_NAME = "Discipline.findById";
  private static final String DISCIPLINE_FIND_ALL_QUERY_NAME = "Discipline.findAll";
  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @GET
  public DisciplinesContainer all() {

    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_ALL_QUERY_NAME, Discipline.class);
    List<Discipline> disciplines = namedQuery.getResultList();

    return new DisciplinesContainer(disciplines);
  }

  @GET
  @Path("{id}")
  public Discipline byId(@PathParam("id") Long id) {

    TypedQuery<Discipline> namedQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_BY_ID_QUERY_NAME, Discipline.class);
    namedQuery.setParameter("id", id);
    List<Discipline> disciplines = namedQuery.getResultList();

    return disciplines.get(0);
  }

  @GET
  @Path("/new")
  public Discipline newDiscipline(@QueryParam("label") String label) {

    Discipline discipline = new Discipline();
    discipline.setLabel(label);

    entityManager.persist(discipline);

    return discipline;
  }

  @GET
  @Path("/{id}/opponents")
  public OpponentsContainer opponents(@PathParam("id") Long id) {
    TypedQuery<Opponent> namedQuery = entityManager.createNamedQuery("Discipline.findOpponents", Opponent.class);
    namedQuery.setParameter("id", id);
    List<Opponent> opponents = namedQuery.getResultList();

    return new OpponentsContainer(opponents);
  }

  @GET
  @Path("/{id}/opponents/add")
  public Opponent opponents(@PathParam("id") Long disciplineId, @QueryParam("opponentId") Long opponentId) {
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

    return opponentToAdd;
  }

  @GET
  @Path("/{id}/restrictions")
  public RestrictionsContainer restrictions(@PathParam("id") Long id) {
    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Discipline.findRestrictions",
        Restriction.class);
    namedQuery.setParameter("id", id);
    List<Restriction> restrictions = namedQuery.getResultList();

    return new RestrictionsContainer(restrictions);
  }

  @GET
  @Path("/{id}/restrictions/add")
  public Restriction addRestrictions(@PathParam("id") Long disciplineId, @QueryParam("restrictionId") Long restrictionId) {
    // load Restriction
    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findById", Restriction.class);
    namedQuery.setParameter("id", restrictionId);
    List<Restriction> restrictions = namedQuery.getResultList();
    Restriction restrictionToAdd = restrictions.get(0);

    // load discipline
    TypedQuery<Discipline> namedDisciplineQuery = entityManager.createNamedQuery(DISCIPLINE_FIND_BY_ID_QUERY_NAME,
        Discipline.class);
    namedDisciplineQuery.setParameter("id", disciplineId);
    List<Discipline> disciplines = namedDisciplineQuery.getResultList();
    Discipline discipline = disciplines.get(0);

    // persist discipline
    discipline.getRestrictions().add(restrictionToAdd);
    entityManager.persist(discipline);

    return restrictionToAdd;
  }

}