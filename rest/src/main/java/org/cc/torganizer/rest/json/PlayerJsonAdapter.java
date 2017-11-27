package org.cc.torganizer.rest.json;

import static org.cc.torganizer.core.entities.Status.ACTIVE;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.adapter.JsonbAdapter;

import org.cc.torganizer.core.entities.Player;

public class PlayerJsonAdapter extends AbstractJsonAdapter<Player, JsonObject>
    implements JsonbAdapter<Player, JsonObject> {

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

    addWithDefault(objectBuilder, "id", player.getId(), "");
    addWithDefault(objectBuilder, "status", player.getStatus(), ACTIVE.toString());
    addWithDefault(objectBuilder, "lastMatch", player.getLastMatch(), "");

    return objectBuilder.add("person", personAdapter.adaptToJson(player.getPerson()))
        .build();
  }

  @Override
  public Player adaptFromJson(JsonObject jo) throws Exception {
    Player player = new Player();

    player.setId(toLong(jo.getString("id", null)));

    return player;
  }

}
