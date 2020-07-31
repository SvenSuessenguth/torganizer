package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Club;

import javax.json.*;
import java.util.Collection;
import java.util.HashMap;

public class ClubJsonConverter extends BaseModelJsonConverter<Club> {

  @Override
  public JsonObject toJsonObject(Club club) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    Long clubId = club != null ? club.getId() : null;
    String clubName = club != null ? club.getName() : null;

    add(objectBuilder, "id", clubId);
    add(objectBuilder, "name", clubName);

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
      Club club = getProperEntity(jsonObject, clubs);
      toModel(jsonObject, club);
    });

    return clubs;
  }
}
