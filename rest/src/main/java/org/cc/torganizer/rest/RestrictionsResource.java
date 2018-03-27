package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.persistence.RestrictionsRepository;
import org.cc.torganizer.rest.json.RestrictionJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import java.util.List;

@Stateless
@Path("restrictions")
@Produces("application/json")
public class RestrictionsResource extends AbstractResource {

  @Inject
  private RestrictionsRepository rRepository;

  @Inject
  private RestrictionJsonConverter converter;

  @POST
  public JsonObject create(JsonObject jsonObject) {

    Restriction restriction = converter.toModel(jsonObject);
    rRepository.create(restriction);

    return converter.toJsonObject(restriction);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {
    Restriction restriction = rRepository.read(id);

    return converter.toJsonObject(restriction);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("length") Integer length) {
    List<Restriction> restrictions = rRepository.read(offset, length);

    return converter.toJsonArray(restrictions);
  }
}
