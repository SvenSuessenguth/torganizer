package org.cc.torganizer.rest.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

/**
 * @author svens
 * 
 * <pre>
 
 * </pre>
 * 
 */
@RequestScoped
public class SquadJsonConverter extends ModelJsonConverter<Squad>{

  @Inject  
  private PlayerJsonConverter playerConverter;
  
  public SquadJsonConverter(){
  }
  
  public SquadJsonConverter(PlayerJsonConverter playerConverter){
    this.playerConverter = playerConverter;
  }
  
  @Override
  public JsonObject toJsonObject(Squad squad) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();
    
    add(objectBuilder, "id", squad.getId());
    objectBuilder.add("players", playerConverter.toJsonArray(squad.getPlayers()));
      
    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Squad> squads) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
    
    squads.forEach(squad -> arrayBuilder.add(this.toJsonObject(squad)) );
    
    return arrayBuilder.build();
  }

  @Override
  public Squad toModel(JsonObject jsonObject) {
    
    Squad squad = new Squad();
    
    String idString = get(jsonObject, "id");
    Long id = idString==null?null:Long.valueOf(idString);
    squad.setId(id);
    
    JsonArray playersJson = jsonObject.getJsonArray("players");
    Collection<Player> players = playerConverter.toModels(playersJson);
    squad.addPlayers(players);
    
    return squad;
  }

  @Override
  public Collection<Squad> toModels(JsonArray jsonArray) {
    List<Squad> squads = new ArrayList<>();
    
    jsonArray.forEach((JsonValue arrayValue) -> squads.add(toModel((JsonObject)arrayValue)));
    
    return squads;
  }
}
