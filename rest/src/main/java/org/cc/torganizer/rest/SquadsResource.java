package org.cc.torganizer.rest;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.SquadsRepository;
import org.cc.torganizer.rest.json.PlayerJsonConverter;
import org.cc.torganizer.rest.json.SquadJsonConverter;

@Stateless
@Path("/squads")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SquadsResource {

  @Inject
  private SquadJsonConverter converter;

  @Inject
  private PlayerJsonConverter playersConverter;

  @Inject
  private SquadsRepository ssquadsRepository;

  @Inject
  private PlayersRepository playersRepository;

  /**
   * Create and persist a new squad.
   */
  @POST
  public JsonObject create(JsonObject jsonObject) {
    Squad squad = converter.toModel(jsonObject, new Squad());

    JsonArray playerArray = jsonObject.getJsonArray("players");
    for (int i = 0; i < playerArray.size(); i++) {
      JsonObject playerObject = playerArray.getJsonObject(i);
      Long playerId = Long.valueOf(playerObject.get("id").toString());
      Player player = playersRepository.read(playerId);
      squad.addPlayer(player);
    }

    squad = ssquadsRepository.create(squad);
    return converter.toJsonObject(squad);
  }

  /**
   * Getting the squad with the given id.
   */
  @GET
  @Path("{id}")
  public JsonObject readSingle(@PathParam("id") Long id) {
    Squad squad = ssquadsRepository.read(id);

    return converter.toJsonObject(squad);
  }

  /**
   * Getting multiple squads from offset to maxResults.
   */
  @GET
  public JsonArray readMultiple(@QueryParam("offset") Integer offset,
                                @QueryParam("maxResults") Integer maxResults) {
    List<Squad> squads = ssquadsRepository.read(offset, maxResults);

    return converter.toJsonArray(squads);
  }

  /**
   * Update an existing squad.
   */
  @PUT
  public JsonObject update(JsonObject jsonObject) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    Squad squad = ssquadsRepository.read(id);
    squad = converter.toModel(jsonObject, squad);
    squad.getPlayers().clear();

    JsonArray playerArray = jsonObject.getJsonArray("players");
    for (int i = 0; i < playerArray.size(); i++) {
      JsonObject playerObject = playerArray.getJsonObject(i);
      Long playerId = Long.valueOf(playerObject.get("id").toString());
      Player player = playersRepository.read(playerId);
      squad.addPlayer(player);
    }

    squad = ssquadsRepository.update(squad);
    return converter.toJsonObject(squad);
  }

  /**
   * Deleting the squad with the given id.
   */
  @DELETE
  @Path("/{id}")
  public JsonObject delete(@PathParam("id") Long id) {
    Squad squad = ssquadsRepository.delete(id);

    return converter.toJsonObject(squad);
  }

  @GET
  @Path("/count")
  public long count() {
    return ssquadsRepository.count();
  }

  /**
   * Getting the players of the squad with the given id.
   */
  @GET
  @Path("/{id}/players")
  public JsonArray players(@PathParam("id") Long squadId) {
    List<Player> players = ssquadsRepository.getPlayers(squadId);

    return playersConverter.toJsonArray(players);
  }
}