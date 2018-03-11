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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.cc.torganizer.core.entities.Restriction;
import static org.cc.torganizer.rest.AbstractResource.DEFAULT_OFFSET;
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
  public JsonObject readSingle(@PathParam("id") Long id) {

    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findById", Restriction.class);
    namedQuery.setParameter("id", id);
    Restriction restrictions = namedQuery.getSingleResult();

    return converter.toJsonObject(restrictions);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {

    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Restriction> namedQuery = entityManager.createNamedQuery("Restriction.findAll", Restriction.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    List<Restriction> restrictions = namedQuery.getResultList();

    return converter.toJsonArray(restrictions);
  }
}
