package org.cc.torganizer.rest;

import static org.cc.torganizer.rest.json.BaseModelJsonConverter.emptyArray;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonNumber;
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
import org.cc.torganizer.rest.json.BaseModelJsonConverter;
import org.cc.torganizer.rest.json.GroupJsonConverter;
import org.cc.torganizer.rest.json.OpponentJsonConverterProvider;
import org.cc.torganizer.rest.json.RoundJsonConverter;

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
  private OpponentToGroupsAssignerFactory oppToGroupAssFactory;

  /**
   * Create and persist a new rouond.
   */
  @POST
  public JsonObject create(JsonObject jsonObject) {
    Round round = roundConverter.toModel(jsonObject, new Round());
    round = roundsRepository.create(round);

    return roundConverter.toJsonObject(round);
  }

  /**
   * Getting the round with the given id.
   */
  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long roundId) {
    Round round = roundsRepository.read(roundId);

    return roundConverter.toJsonObject(round);
  }

  /**
   * Getting multiple rounds from offset to maxResults.
   */
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    List<Round> roounds = roundsRepository.read(offset, maxResults);
    return roundConverter.toJsonArray(roounds);
  }

  /**
   * Update the round with the given id.
   */
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Round round = roundsRepository.read(id);

    round = roundConverter.toModel(jsonObject, round);
    roundsRepository.update(round);

    return roundConverter.toJsonObject(round);
  }

  /**
   * Delete the round with the given id.
   */
  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Round round = roundsRepository.read(id);
    round = roundsRepository.delete(round);

    return roundConverter.toJsonObject(round);
  }

  /**
   * Getting the groups from the round with the given id from offset to maxResults.
   */
  @GET
  @Path("/{id}/groups")
  public Response getGroupsByRound(@PathParam("id") Long roundId,
                                   @QueryParam("offset") Integer offset,
                                   @QueryParam("maxResults") Integer maxResults) {
    Round round = roundsRepository.read(roundId);
    List<Group> groups = round.getGroups();
    JsonArray jsonGroups = groupConverter.toJsonArray(groups);

    // adding Opponents-Json to Group-Json
    JsonArray patchedJsonGroups = addOpponentsToGroupsJson(jsonGroups, groups);

    return Response.ok(patchedJsonGroups).build();
  }

  /**
   * Create and persist a new group for the round with the given id.
   */
  @POST
  @Path("/{roundId}/group")
  public Response newGroup(@PathParam("roundId") Long roundId) {
    List<Group> groups = roundsRepository.newGroup(roundId);
    JsonArray jsonArray = groupConverter.toJsonArray(groups);

    return Response.ok(jsonArray).build();
  }

  /**
   * Remove and delete the  first group from the round with the given roundId.
   */
  @DELETE
  @Path("/{roundId}/group")
  public Response deleteGroup(@PathParam("roundId") Long roundId) {
    List<Group> groups = roundsRepository.deleteGroup(roundId);
    JsonArray jsonArray = groupConverter.toJsonArray(groups);

    return Response.ok(jsonArray).build();
  }

  /**
   * Getting all opponents, which can be assigned to the groups of the round with the given id.
   */
  @SuppressWarnings("unchecked")
  @GET
  @Path("/{id}/opponents-assignable-to-group")
  public JsonArray getOpponentsAssignableToGroup(@PathParam("id") Long roundId) {
    JsonArray result;

    Set<Opponent> opponents = roundsRepository.getNotAssignedOpponents(roundId);

    // all opponents must have same type (see opponentTypeRestriction)
    if (opponents.isEmpty()) {
      result = emptyArray();
    } else {
      BaseModelJsonConverter<Opponent> opponentConverter =
          opponentJsonConverterProvider.getConverter(opponents);
      result = opponentConverter.toJsonArray(opponents);
    }

    return result;
  }

  /**
   * Assign all opponents to the groups of the round with the given id.
   */
  @POST
  @Path("/{id}/auto-assign-opponents")
  public Response autoAssignOpponents(@PathParam("id") Long roundId) {

    Round round = roundsRepository.read(roundId);
    System system = round.getSystem();
    Set<Opponent> opponents = roundsRepository.getNotAssignedOpponents(roundId);
    List<Group> groups = round.getGroups();
    JsonArray jsonGroups = groupConverter.toJsonArray(groups);

    OpponentToGroupsAssigner assigner = oppToGroupAssFactory.getOpponentToGroupsAssigner(system);
    assigner.assign(opponents, groups);

    // adding Opponents-Json to Group-Json
    JsonArray patchedJsonGroups = addOpponentsToGroupsJson(jsonGroups, groups);

    return Response.ok(patchedJsonGroups).build();
  }

  private Group getGroupById(Collection<Group> groups, Long id) {
    for (Group group : groups) {
      if (Objects.equals(group.getId(), id)) {
        return group;
      }
    }

    throw new GroupNotFoundException();
  }

  /**
   * adding opponents-json to groups-json.
   */
  protected JsonArray addOpponentsToGroupsJson(JsonArray jsonGroups, Collection<Group> groups) {
    // adding opponents-json to groups-json
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    int size = jsonGroups.size();
    for (int index = 0; index < size; index++) {
      JsonObject groupJson = jsonGroups.getJsonObject(index);
      JsonNumber jsonNumber = groupJson.getJsonNumber("id");
      Long id = jsonNumber.longValue();
      Group group = getGroupById(groups, id);
      JsonObject patchedGroupJson = groupConverter.addOpponents(groupJson, group.getOpponents());
      arrayBuilder.add(patchedGroupJson);
    }
    return arrayBuilder.build();
  }
}
