package org.cc.torganizer.rest.json;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

public class PlayerJsonConverter extends AbstractJsonConverter implements JsonConverter<Player> {
  
  private PersonJsonConverter personConverter = new PersonJsonConverter();

  @Override
  public JsonObject toJson(Player player) {
    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

    objectBuilder.add("status", player.getStatus().toString()).add("person",
        personConverter.toJson(player.getPerson()));

    addIfNotNull(objectBuilder, "lastMatch", player.getLastMatch());
    addIfNotNull(objectBuilder, "id", player.getId());

    return objectBuilder.build();

  }

  @Override
  public JsonObject toJson(List<Player> objects) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Player toObject(JsonObject jsonObject) {
    Player player = new Player();
     
    Long id = getLong(jsonObject, "id");
    player.setId(id);
    
    Person person = personConverter.toObject(jsonObject.getJsonObject("person"));
    player.setPerson(person);
    
    return player;
  }

  @Override
  public List<Player> toList(JsonObject jsonObject) {
    // TODO Auto-generated method stub
    return null;
  }

}
