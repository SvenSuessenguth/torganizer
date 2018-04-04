package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.Squad;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.*;
import java.util.Collection;
import java.util.HashMap;

import static org.cc.torganizer.core.entities.OpponentType.SQUAD;

/**
 * @author svens
 */
@RequestScoped
public class SquadJsonConverter extends ModelJsonConverter<Squad> implements OpponentJsonConverter{

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
  public Squad toModel(JsonObject jsonObject, Squad squad) {
    return squad;
  }

  @Override
  public Collection<Squad> toModels(JsonArray jsonArray, Collection<Squad> squads) {
    return squads;
  }

  @Override
  public OpponentType getOpponentType() {
    return SQUAD;
  }
}
