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
  private RestrictionsRepository restrictionsRepository;

  @Inject
  private RestrictionJsonConverter converter;

  /**
   * Create and persist a new restriction.
   */
  @POST
  public JsonObject create(JsonObject jsonObject) {
    String discriminatorId = jsonObject.getString("discriminator");
    Restriction.Discriminator discriminator = Restriction.Discriminator.byId(discriminatorId);
    Restriction restriction = discriminator.create();

    restriction = converter.toModel(jsonObject, restriction);
    restrictionsRepository.create(restriction);

    return converter.toJsonObject(restriction);
  }

  /**
   * Getting the restriction with the given id.
   */
  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {
    Restriction restriction = restrictionsRepository.read(id);

    return converter.toJsonObject(restriction);
  }

  /**
   * Getting multiple restrictions from offset to maxResults.
   */
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    List<Restriction> restrictions = restrictionsRepository.read(offset, maxResults);

    return converter.toJsonArray(restrictions);
  }
}
