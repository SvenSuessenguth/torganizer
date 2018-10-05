package org.cc.torganizer.rest.json;

import java.util.Collection;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.cc.torganizer.core.entities.Tournament;

/**
 * @author svens
 */
public class TournamentJsonConverter extends ModelJsonConverter<Tournament> {

  @Override
  public JsonObject toJsonObject(Tournament tournament) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    add(objectBuilder, "id", tournament.getId());
    add(objectBuilder, "name", tournament.getName());

    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Tournament> tournaments) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    tournaments.forEach(tournament -> arrayBuilder.add(this.toJsonObject(tournament)));

    return arrayBuilder.build();
  }

  @Override
  public Tournament toModel(JsonObject jsonObject, Tournament tournament) {
    String name = jsonObject.getString("name");
    tournament.setName(name);

    return tournament;
  }

  @Override
  public Collection<Tournament> toModels(JsonArray jsonArray, Collection<Tournament> tournaments) {
    jsonArray.forEach(item -> {
      JsonObject jsonObject = (JsonObject) item;
      Tournament tournament = getProperEntity(jsonObject, tournaments);
      toModel(jsonObject, tournament);
    });

    return tournaments;
  }
}
