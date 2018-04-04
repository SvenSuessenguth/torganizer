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
    Person person = converter.toModel(jsonObject, new Person());
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
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Person person = pRepository.read(id);

    person = converter.toModel(jsonObject, person);
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