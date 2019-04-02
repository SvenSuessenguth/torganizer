package org.cc.torganizer.rest;

import static org.cc.torganizer.rest.json.BaseModelJsonConverter.emptyArray;

import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.PositionalOpponent;
import org.cc.torganizer.persistence.GroupsRepository;
import org.cc.torganizer.rest.json.BaseModelJsonConverter;
import org.cc.torganizer.rest.json.GroupJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;
import org.cc.torganizer.rest.json.PositionalOpponentJsonConverter;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Stateless
@Path("/groups")
@Produces("application/json")
public class GroupsResource extends AbstractResource {

  @Inject
  private GroupsRepository groupsRepo;

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  @Inject
  private PositionalOpponentJsonConverter positionlOpponentJsonConverter;

  @Inject
  private GroupJsonConverter groupConverter;

  @Operation(operationId = "createGroup")
  @POST
  public JsonObject create(JsonObject jsonObject) {
    return null;
  }

  @Operation(operationId = "readSingleGroup")
  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long groupId) {
    return null;
  }

  @Operation(operationId = "readMultipleGroups")
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    return null;
  }

  @Operation(operationId = "updateGroup")
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    return null;
  }

  @Operation(operationId = "getOpponentByGroup")
  @GET
  @Path("/{id}/opponents")
  public JsonArray getOpponents(@PathParam("id") Long groupId,
                                @QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {

    JsonArray result = null;
    List<PositionalOpponent> positionalOpponents = groupsRepo.getPositionalOpponents(groupId,
        offset, maxResults);

    // all opponents must have same type (see opponentTypeRestriction)
    if (positionalOpponents.isEmpty()) {
      return emptyArray();
    } else {
      result = positionlOpponentJsonConverter.toJsonArray(positionalOpponents);
    }

    return result;
  }

  @Operation(operationId = "addOpponentToGroup")
  @POST
  @Path("/{id}/opponents")
  public JsonObject addOpponent(@PathParam("id") Long groupId,
                                @QueryParam("opponentId") Long opponentId) {
    Group group = groupsRepo.addOpponent(groupId, opponentId);

    return groupConverter.toJsonObject(group);
  }

  @Operation(operationId = "removeGroupOpponent")
  @DELETE
  @Path("/{id}/opponents")
  public JsonObject removeOpponent(@PathParam("id") Long groupId,
                                   @QueryParam("opponentId") Long opponentId) {
    return null;
  }

  @SuppressWarnings("unchecked")
  @GET
  @Path("/{id}/assignableOpponents")
  public JsonArray getAssignableOpponents(@PathParam("id") Long groupId) {
    JsonArray result = null;

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
}
