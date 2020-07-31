package org.cc.torganizer.rest;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.PositionalOpponent;
import org.cc.torganizer.persistence.GroupsRepository;
import org.cc.torganizer.rest.json.BaseModelJsonConverter;
import org.cc.torganizer.rest.json.GroupJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;
import org.cc.torganizer.rest.json.PositionalOpponentJsonConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.cc.torganizer.rest.json.BaseModelJsonConverter.emptyArray;

@Stateless
@Path("/groups")
@Produces("application/json")
public class GroupsResource extends AbstractResource {

  @Inject
  private GroupsRepository groupsRepo;

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  @Inject
  private PositionalOpponentJsonConverter positionalOpponentJsonConverter;

  @Inject
  private GroupJsonConverter groupConverter;

  @POST
  public JsonObject create(JsonObject jsonObject) {
    return null;
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long groupId) {
    return null;
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    return null;
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    return null;
  }

  /**
   * Get the opponents from offset to maxResults from the group with the given id.
   */
  @GET
  @Path("/{id}/opponents")
  public JsonArray getOpponents(@PathParam("id") Long groupId,
                                @QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {

    JsonArray result;
    List<PositionalOpponent> positionalOpponents = groupsRepo.getPositionalOpponents(groupId,
      offset, maxResults);

    // all opponents must have same type (see opponentTypeRestriction)
    if (positionalOpponents.isEmpty()) {
      return emptyArray();
    } else {
      result = positionalOpponentJsonConverter.toJsonArray(positionalOpponents);
    }

    return result;
  }

  /**
   * Adding the opponent with the given id to the group with the given id.
   */
  @POST
  @Path("/{id}/opponents")
  public JsonObject addOpponent(@PathParam("id") Long groupId,
                                @QueryParam("opponentId") Long opponentId) {
    Group group = groupsRepo.addOpponent(groupId, opponentId);

    return groupConverter.toJsonObject(group);
  }

  @DELETE
  @Path("/{id}/opponents")
  public JsonObject removeOpponent(@PathParam("id") Long groupId,
                                   @QueryParam("opponentId") Long opponentId) {
    return null;
  }

  /**
   * Getting the assignable opponents for the group with the given id.
   */
  @SuppressWarnings("unchecked")
  @GET
  @Path("/{id}/assignableOpponents")
  public JsonArray getAssignableOpponents(@PathParam("id") Long groupId) {
    JsonArray result;

    Set<Opponent> opponents = groupsRepo.getAssignableOpponents(groupId);

    // all opponents must have same type (see opponentTypeRestriction)
    if (opponents.isEmpty()) {
      return emptyArray();
    } else {
      BaseModelJsonConverter<Opponent> opponentConverter =
        opponentJsonConverterProvider.getConverter(opponents);
      result = opponentConverter.toJsonArray(opponents);
    }

    return result;
  }

  /**
   * Returns an array of both groups including the opponents.
   */
  @PUT
  @Path("moveOpponent")
  public Response moveOpponent(@QueryParam("from") Long fromGroupId,
                               @QueryParam("to") Long toGroupId,
                               @QueryParam("opponent") Long opponentId) {
    List<Group> groups = new ArrayList<>();
    JsonArray json = groupConverter.toJsonArray(groups);

    return Response.ok(json, MediaType.APPLICATION_JSON).build();
  }
}
