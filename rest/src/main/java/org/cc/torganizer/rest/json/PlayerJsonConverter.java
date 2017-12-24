package org.cc.torganizer.rest.json;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

/**
 * @author svens
 */
@RequestScoped
public class PlayerJsonConverter extends ModelJsonConverter<Player>{

  @Inject  
  private PersonJsonConverter personConverter;
  
  @Override
  public JsonObject toJsonObject(Player player) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();
    
    add(objectBuilder, "id", player.getId());
    add(objectBuilder, "lastMatch", player.getLastMatch());
    add(objectBuilder, "person", personConverter.toJsonObject(player.getPerson()));
      
    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Player> players) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
    
    players.forEach((player) -> {
      arrayBuilder.add(this.toJsonObject(player));
    });
    
    return arrayBuilder.build();
  }

  @Override
  public Player toModel(JsonObject jsonObject) {
    
    Player player = new Player();
    
    String idString = get(jsonObject, "id");
    Long id = idString==null?null:Long.valueOf(idString);
    player.setId(id);
    
    String lastMatchString = get(jsonObject, "lastMatch");
    LocalDateTime lastMatch = super.localDateTimeFromString(lastMatchString);
    player.setLastMatch(lastMatch);
    
    JsonObject personJsonObject = jsonObject.getJsonObject("person");
    final Person person = personConverter.toModel(personJsonObject);
    player.setPerson(person);
    
    return player;
  }

  @Override
  public Collection<Player> toModels(JsonArray jsonArray) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
}
