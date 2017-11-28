package org.cc.torganizer.rest.json;

import static org.cc.torganizer.core.entities.Status.ACTIVE;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.adapter.JsonbAdapter;

import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Status;

public class PlayerJsonAdapter implements AbstractJsonAdapter, JsonbAdapter<Player, JsonObject> {

  private PersonJsonAdapter personAdapter = new PersonJsonAdapter();

  /**
   * <code>
   * {
   *   "id": playerId,
   *   "status": status,
   *   "person":{
   *     "id": personId,
   *     "firstName": firstName,
   *     "lastName": lastName,
   *     "dateOfBirthISO": dateOfBirth,
   *     "gender": gender
   *   }
   * </code>
   */
  @Override
  public JsonObject adaptToJson(Player player) throws Exception {
    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

    addWithEmptyDefault(objectBuilder, "id", player.getId());
    addWithDefault(objectBuilder, "status", player.getStatus(), ACTIVE);
    addWithEmptyDefault(objectBuilder, "lastMatch", player.getLastMatch());

    return objectBuilder.add("person", personAdapter.adaptToJson(player.getPerson()))
        .build();
  }

  @Override
  public Player adaptFromJson(JsonObject jo) throws Exception {
    Player player = new Player();

    player.setId(longFromString(jo.getString("id", null)));
    player.setStatus(Status.valueOf(jo.getString("status", ACTIVE.toString())));

    return player;
  }

}
