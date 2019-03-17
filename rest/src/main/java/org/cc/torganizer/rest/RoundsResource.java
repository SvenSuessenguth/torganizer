package org.cc.torganizer.rest;

import static org.cc.torganizer.rest.json.ModelJsonConverter.emptyArray;

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
import javax.ws.rs.core.Response;

import org.cc.torganizer.core.OpponentToGroupsAssigner;
import org.cc.torganizer.core.OpponentToGroupsAssignerFactory;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;
import org.cc.torganizer.persistence.RoundsRepository;
import org.cc.torganizer.rest.json.GroupJsonConverter;
import org.cc.torganizer.rest.json.ModelJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;
import org.cc.torganizer.rest.json.RoundJsonConverter;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Stateless
@Path("/rounds")
@Produces("application/json")
public class RoundsResource extends AbstractResource {

  @Inject
  private RoundsRepository roundsRepository;

  @Inject
  private RoundJsonConverter roundConverter;

  @Inject
  private GroupJsonConverter groupConverter;

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  @Inject
  private OpponentToGroupsAssignerFactory opponentToGroupsAssignerFactory;

  @Operation(operationId = "createRound")
  @POST
  public JsonObject create(JsonObject jsonObject) {
    Round round = roundConverter.toModel(jsonObject, new Round());
    round = roundsRepository.create(round);

    return roundConverter.toJsonObject(round);
  }

  @Operation(operationId = "readSingleRound")
  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long roundId) {
    Round round = roundsRepository.read(roundId);

    return roundConverter.toJsonObject(round);
  }

  @Operation(operationId = "readMultipleRounds")
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    List<Round> roounds = roundsRepository.read(offset, maxResults);
    return roundConverter.toJsonArray(roounds);
  }

  @Operation(operationId = "updateRound")
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Round round = roundsRepository.read(id);

    round = roundConverter.toModel(jsonObject, round);
    roundsRepository.update(round);

    return roundConverter.toJsonObject(round);
  }

  @Operation(operationId = "deleteRound")
  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Round round = roundsRepository.read(id);
    round = roundsRepository.delete(round);

    return roundConverter.toJsonObject(round);
  }

  @Operation(operationId = "getGroupsByRound")
  @GET
  @Path("/{id}/groups")
  public Response getGroupsByRound(@PathParam("id") Long roundId,
                                   @QueryParam("offset") Integer offset,
                                   @QueryParam("maxResults") Integer maxResults) {
    Round round = roundsRepository.read(roundId);
    JsonArray jsonArray = groupConverter.toJsonArray(round.getGroups());

    return Response.ok(jsonArray).build();
  }

  @Operation(operationId = "createGroupForRound")
  @POST
  @Path("/{id}/group")
  public Response newGroup(@PathParam("id") Long id) {
    List<Group> groups = roundsRepository.newGroup(id);
    JsonArray jsonArray = groupConverter.toJsonArray(groups);

    return Response.ok(jsonArray).build();
  }

  @Operation(operationId = "deleteGroupFromRound")
  @DELETE
  @Path("/{id}/group")
  public Response deleteGroup(@PathParam("id") Long id) {
    List<Group> groups = roundsRepository.deleteGroup(id);
    JsonArray jsonArray = groupConverter.toJsonArray(groups);

    return Response.ok(jsonArray).build();
  }

  @SuppressWarnings("unchecked")
  @Operation(operationId = "getOpponentsAssignableToGroupForRound")
  @GET
  @Path("/{id}/opponents-assignable-to-group")
  public JsonArray getOpponentsAssignableToGroup(@PathParam("id") Long roundId) {
    JsonArray result;

    Set<Opponent> opponents = roundsRepository.getNotAssignedOpponents(roundId);

    // all opponents must have same type (see opponentTypeRestriction)
    if (opponents.isEmpty()) {
      result = emptyArray();
    } else {
      ModelJsonConverter<Opponent> opponentConverter =
          opponentJsonConverterProvider.getConverter(opponents);
      result = opponentConverter.toJsonArray(opponents);
    }

    return result;
  }

  @Operation(operationId = "autoAssignOpponents")
  @POST
  @Path("/{id}/auto-assign-opponents")
  public Response autoAssignOpponents(@PathParam("id") Long roundId) {

    Round round = roundsRepository.read(roundId);
    System system = round.getSystem();
    Set<Opponent> opponents = roundsRepository.getNotAssignedOpponents(roundId);
    List<Group> groups = round.getGroups();

    OpponentToGroupsAssigner assigner = opponentToGroupsAssignerFactory.getOpponentToGroupsAssigner(system);
    assigner.assign(opponents, groups);

    JsonArray jsonArray = groupConverter.toJsonArray(groups);
    return Response.ok(jsonArray).build();
  }
}
