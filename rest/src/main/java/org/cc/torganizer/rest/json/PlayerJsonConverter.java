package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Status;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.cc.torganizer.core.entities.OpponentType.PLAYER;

/**
 * @author svens
 */
@RequestScoped
public class PlayerJsonConverter extends ModelJsonConverter<Player> implements OpponentJsonConverter{

  @Inject  
  private PersonJsonConverter personConverter;

  public PlayerJsonConverter(){
  }
  
  public PlayerJsonConverter(PersonJsonConverter personConverter){
    this.personConverter = personConverter;
  }
  
  @Override
  public JsonObject toJsonObject(Player player) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();
    
    add(objectBuilder, "id", player.getId());
    add(objectBuilder, "lastMatch", player.getLastMatch());
    add(objectBuilder, "status", player.getStatus().toString());
    add(objectBuilder, "person", personConverter.toJsonObject(player.getPerson()));
      
    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Player> players) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    players.forEach(player -> arrayBuilder.add(this.toJsonObject(player)) );
    
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
    
    String statusString = get(jsonObject, "status");
    Status status = statusString==null||statusString.trim().isEmpty()?Status.ACTIVE:Status.valueOf(statusString);
    player.setStatus(status);
   
    JsonObject personJsonObject = jsonObject.getJsonObject("person");
    final Person person = personConverter.toModel(personJsonObject);
    player.setPerson(person);
    
    return player;
  }

  @Override
  public Collection<Player> toModels(JsonArray jsonArray) {
    List<Player> players = new ArrayList<>();
    
    jsonArray.forEach((JsonValue arrayValue) -> players.add(toModel((JsonObject)arrayValue)));
    
    return players;
  }

  @Override
  public OpponentType getOpponentType() {
    return PLAYER;
  }
}
