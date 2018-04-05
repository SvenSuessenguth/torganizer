package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Tournament;

import javax.json.*;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author svens
 */
public class ClubJsonConverter extends ModelJsonConverter<Club>{

  @Override
  public JsonObject toJsonObject(Club club) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();
    
    add(objectBuilder, "id", club.getId());
    add(objectBuilder, "name", club.getName());
      
    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Club> clubs) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();
    
    clubs.forEach(club -> arrayBuilder.add(this.toJsonObject(club)));
    
    return arrayBuilder.build();
  }

  @Override
  public Club toModel(JsonObject jsonObject, Club club) {
    String name = jsonObject.getString("name");
    club.setName(name);

    return club;
  }

  @Override
  public Collection<Club> toModels(JsonArray jsonArray, Collection<Club> clubs) {
    jsonArray.forEach(item -> {
      JsonObject jsonObject = (JsonObject) item;
      Club club = getProperModel(jsonObject, clubs);
      toModel(jsonObject, club);
    });

    return clubs;
  }
}
