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
import org.cc.torganizer.persistence.PersonsRepository;
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

  @Inject
  private PersonsRepository pRepository;

  @Inject
  private PersonJsonConverter converter;
  
  @POST
  public Response create(JsonObject jsonObject, @Context UriInfo uriInfo) {
    Person person = converter.toModel(jsonObject);
    pRepository.create(person);

    final JsonObject result = converter.toJsonObject(person);
    URI uri = uriInfo.getAbsolutePathBuilder().path(""+person.getId()).build();
    
    return created(uri).entity(result).build();
  }

  @GET
  @Path("/{id}")
  public JsonObject read(@PathParam("id") Long id) {
    Person person = pRepository.read(id);

    return converter.toJsonObject(person);
  }

  @PUT
  @Path("/{id}")
  public JsonObject update(JsonObject jsonObject) {
    Person person = converter.toModel(jsonObject);
    pRepository.update(person);

    return converter.toJsonObject(person);
  }

  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Person person = pRepository.delete(id);

    return converter.toJsonObject(person);
  }

  @GET
  @Path("/all")
  public JsonArray all(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    List<Person> persons = pRepository.read(offset, length);

    return converter.toJsonArray(persons);
  }

  @GET
  @Path("/count")
  public long count() {
    return pRepository.count();
  }
}