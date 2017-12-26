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
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.rest.json.PersonJsonConverter;

/**
 * http://www.restapitutorial.com/lessons/httpmethods.html
 * @author svens
 */
@Stateless
@Path("/persons")
@Produces("application/json")
@Consumes("application/json")
public class PersonsResource extends AbstractResource {

  @PersistenceContext(name = "torganizer")
  EntityManager entityManager;

  @Inject
  private PersonJsonConverter converter;
  
  @POST
  @Path("/")
  public Response create(JsonObject jsonObject, @Context UriInfo uriInfo) throws Exception{
    Person person = converter.toModel(jsonObject);
    // Person wird als nicht-persistente entity betrachtet.
    // vom client wird die id '0' geliefert, sodass eine detached-entity-Exception geworfen wird.
    person.setId(null);
    entityManager.persist(person);

    final JsonObject result = converter.toJsonObject(person);
    URI uri = uriInfo.getAbsolutePathBuilder().path(""+person.getId()).build();
    
    return created(uri).entity(result).build();
  }

  @GET
  @Path("/{id}")
  public JsonObject read(@PathParam("id") Long id) {

    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findById", Person.class);
    namedQuery.setParameter("id", id);
    Person person = namedQuery.getSingleResult();
    
    return converter.toJsonObject(person);
  }

  @PUT
  @Path("/{id}")
  public JsonObject update(JsonObject jsonObject) {
    Person person = converter.toModel(jsonObject);
    entityManager.merge(person);

    return converter.toJsonObject(person);
  }

  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findById", Person.class);
    namedQuery.setParameter("id", id);
    List<Person> persons = namedQuery.getResultList();

    Person personToDelete = persons.get(0);
    entityManager.remove(personToDelete);

    return converter.toJsonObject(personToDelete);
  }

  @GET
  @Path("/all")
  public JsonArray all(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {

    if (offset == null || length == null) {
      offset = DEFAULT_OFFSET;
      length = DEFAULT_LENGTH;
    }

    TypedQuery<Person> namedQuery = entityManager.createNamedQuery("Person.findAll", Person.class);
    namedQuery.setFirstResult(offset);
    namedQuery.setMaxResults(length);
    List<Person> persons = namedQuery.getResultList();

    return converter.toJsonArray(persons);
  }

  @GET
  @Path("/count")
  public long count() {
    Query query = entityManager.createQuery("SELECT count(p) FROM Person p");
    return (long) query.getSingleResult();
  }
}