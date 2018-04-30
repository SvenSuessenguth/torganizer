package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.rest.json.ClubJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import java.util.List;

@Stateless
@Path("/clubs")
@Produces("application/json")
@Consumes("application/json")
public class ClubsResource extends AbstractResource {
  @Inject
  private ClubsRepository cRepository;

  @Inject
  private ClubJsonConverter cConverter;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    Club club = cConverter.toModel(jsonObject, new Club());
    // client can send '0' with a detached object exception as the result
    club.setId(null);

    club = cRepository.create(club);

    return cConverter.toJsonObject(club);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {
    Club club = cRepository.read(id);
    return cConverter.toJsonObject(club);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset, @QueryParam("maxResults") Integer maxResults) {
    List<Club> clubs = cRepository.read(offset, maxResults);

    return cConverter.toJsonArray(clubs);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Club club = cRepository.read(id);

    club = cConverter.toModel(jsonObject, club);
    cRepository.update(club);

    return cConverter.toJsonObject(club);
  }

  @DELETE
  public JsonObject delete(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Club club = cRepository.read(id);

    cRepository.delete(club);

    return cConverter.toJsonObject(club);
  }
}
