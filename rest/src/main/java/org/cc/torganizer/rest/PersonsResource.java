package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.persistence.PersonsRepository;
import org.cc.torganizer.rest.json.PersonJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.Response.created;

/**
 * http://www.restapitutorial.com/lessons/httpmethods.html
 *
 * @author svens
 */
@Stateless
@Path("/persons")
@Produces("application/json")
@Consumes("application/json")
public class PersonsResource extends AbstractResource {

  @Inject
  private PersonsRepository personsRepository;

  @Inject
  private PersonJsonConverter converter;

  /**
   * Create e new Person.
   */
  @POST
  public Response create(JsonObject jsonObject, @Context UriInfo uriInfo) {
    Person person = converter.toModel(jsonObject, new Person());
    personsRepository.create(person);

    final JsonObject result = converter.toJsonObject(person);
    URI uri = uriInfo.getAbsolutePathBuilder().path("" + person.getId()).build();

    return created(uri).entity(result).build();
  }

  /**
   * Read the person with the given id.
   */
  @GET
  @Path("/{id}")
  public JsonObject read(@PathParam("id") Long id) {
    Person person = personsRepository.read(id);

    return converter.toJsonObject(person);
  }

  /**
   * Update a person.
   */
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Person person = personsRepository.read(id);

    person = converter.toModel(jsonObject, person);
    personsRepository.update(person);

    return converter.toJsonObject(person);
  }

  /**
   * Delete e person.
   */
  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Person person = personsRepository.delete(id);

    return converter.toJsonObject(person);
  }

  /**
   * Getting persons from offset to maxResults.
   */
  @GET
  @Path("/all")
  public JsonArray all(@QueryParam("offset") Integer offset,
                       @QueryParam("maxResults") Integer maxResults) {
    List<Person> persons = personsRepository.read(offset, maxResults);

    return converter.toJsonArray(persons);
  }

  /**
   * Counts all persons.
   */
  @GET
  @Path("/count")
  public long count() {
    return personsRepository.count();
  }
}