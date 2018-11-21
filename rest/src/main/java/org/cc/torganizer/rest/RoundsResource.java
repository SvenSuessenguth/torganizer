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

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.persistence.RoundsRepository;
import org.cc.torganizer.rest.json.GroupJsonConverter;
import org.cc.torganizer.rest.json.ModelJsonConverter;
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

  @POST
  public JsonObject create(JsonObject jsonObject) {
    Round round = roundConverter.toModel(jsonObject, new Round());
    round = roundsRepository.create(round);

    return roundConverter.toJsonObject(round);
  }

  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long roundId) {
    Round round = roundsRepository.read(roundId);

    return roundConverter.toJsonObject(round);
  }

  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    List<Round> roounds = roundsRepository.read(offset, maxResults);
    return roundConverter.toJsonArray(roounds);
  }

  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Round round = roundsRepository.read(id);

    round = roundConverter.toModel(jsonObject, round);
    roundsRepository.update(round);

    return roundConverter.toJsonObject(round);
  }

  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Round round = roundsRepository.read(id);
    round = roundsRepository.delete(round);

    return roundConverter.toJsonObject(round);
  }

  @GET
  @Path("/{id}/opponents")
  public JsonArray getOpponents(@PathParam("id") Long disciplineId,
                                @QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    return null;
  }

  @POST
  @Path("/{id}/opponents")
  public JsonObject addOpponent(@PathParam("id") Long disciplineId,
                                @QueryParam("opponentId") Long opponentId) {
    return null;
  }

  @POST
  @Path("/{id}/create-groups")
  public Response createGroups(@PathParam("id") Long id, @QueryParam("numberOfGroups") Integer numberOfGroups){
    List<Group> groups = roundsRepository.createGroups(id, numberOfGroups);
    JsonArray jsonArray = groupConverter.toJsonArray(groups);

    return Response.ok(jsonArray).build();
  }

  @DELETE
  @Path("/{id}/opponents")
  public JsonObject removeOpponent(@PathParam("id") Long disciplineId,
                                   @QueryParam("opponentId") Long opponentId) {
    return null;
  }

  @SuppressWarnings("unchecked")
  @GET
  @Path("/{id}/opponents-assignable-to-group")
  public JsonArray getOpponentsAssignableToGroup(@PathParam("id") Long roundId) {
    JsonArray result = null;

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
}
