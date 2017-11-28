package org.cc.torganizer.rest.json;

import static org.cc.torganizer.core.entities.Status.ACTIVE;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Status;

public class PlayerJsonConverter implements AbstractJsonConverter{

  private PersonJsonConverter personConverter = new PersonJsonConverter();

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
  public JsonObject toJson(Player player) throws Exception {
    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

    addWithEmptyDefault(objectBuilder, "id", player.getId());
    addWithDefault(objectBuilder, "status", player.getStatus(), ACTIVE);
    addWithEmptyDefault(objectBuilder, "lastMatch", player.getLastMatch());

    return objectBuilder.add("person", personConverter.toJson(player.getPerson()))
        .build();
  }

  public Player fromJson(JsonObject jo) throws Exception {
    Player player = new Player();

    player.setId(longFromString(jo.getString("id", null)));
    player.setStatus(Status.valueOf(jo.getString("status", ACTIVE.toString())));

    return player;
  }

}
