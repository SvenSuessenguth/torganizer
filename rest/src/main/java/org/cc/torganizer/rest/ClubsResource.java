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
  private ClubsRepository clubsRepo;

  @Inject
  private ClubJsonConverter clubsConverter;

  /**
   * Creating and persist an new Club.
   */
  @POST
  public JsonObject create(JsonObject jsonObject) {
    Club club = clubsConverter.toModel(jsonObject, new Club());
    // client can send '0' with a detached object exception as the result
    club.setId(null);

    club = clubsRepo.create(club);

    return clubsConverter.toJsonObject(club);
  }

  /**
   * Reading the club with the given id.
   */
  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {
    Club club = clubsRepo.read(id);
    return clubsConverter.toJsonObject(club);
  }

  /**
   * Reading multiple (offset to maxResult) clubs.
   */
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {

    List<Club> clubs = clubsRepo.read(offset, maxResults);

    return clubsConverter.toJsonArray(clubs);
  }

  /**
   * Updatding an existing club.
   */
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Club club = clubsRepo.read(id);

    club = clubsConverter.toModel(jsonObject, club);
    clubsRepo.update(club);

    return clubsConverter.toJsonObject(club);
  }

  /**
   * Deleting a club.
   */
  @DELETE
  public JsonObject delete(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Club club = clubsRepo.read(id);

    clubsRepo.delete(club);

    return clubsConverter.toJsonObject(club);
  }
}
