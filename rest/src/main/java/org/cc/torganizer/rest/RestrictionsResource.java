package org.cc.torganizer.rest;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.rest.json.RestrictionJsonConverter;

@Stateless
@Path("restrictions")
@Produces("application/json")
public class RestrictionsResource extends AbstractResource {

  @Inject
  private RestrictionJsonConverter converter;

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @POST
  public JsonObject create(JsonObject jsonObject) {

    Restriction restriction = converter.toModel(jsonObject);
    entityManager.persist(restriction);
    entityManager.flush();

    return converter.toJsonObject(restriction);
  }

  @GET
  @Path("{id}")
  public JsonObject byId(@PathParam("id") Long id) {

    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findById", Restriction.class);
    namedQuery.setParameter("id", id);
    Restriction restrictions = namedQuery.getSingleResult();

    return converter.toJsonObject(restrictions);
  }
}
