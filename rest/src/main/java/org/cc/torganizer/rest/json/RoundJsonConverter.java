package org.cc.torganizer.rest.json;

import java.util.Collection;
import java.util.HashMap;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;

/**
 * A json-disciplines contains nothing but id, name and restrictions.
 */
@RequestScoped
public class RoundJsonConverter extends ModelJsonConverter<Round> {

  public static final String PROPERTY_SYSTEM_NAME = "system";

  @Override
  public JsonObject toJsonObject(Round round) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    add(objectBuilder, "id", round.getId());
    add(objectBuilder, "qualified", round.getQualified());
    add(objectBuilder, "position", round.getPosition());

    String systemName = round.getSystem() == null ? null : round.getSystem().name();
    add(objectBuilder, PROPERTY_SYSTEM_NAME, systemName);

    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Round> rounds) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    rounds.forEach(round -> arrayBuilder.add(this.toJsonObject(round)));

    return arrayBuilder.build();
  }

  @Override
  public Round toModel(JsonObject jsonObject, Round round) {
    Integer qualified = jsonObject.getInt("qualified", 0);
    round.setQualified(qualified);

    JsonValue positionValue = jsonObject.get("position");
    Integer position = null;
    if (!JsonValue.NULL.equals(positionValue) && positionValue != null) {
      position = Integer.valueOf(positionValue.toString());
    }
    round.setPosition(position);

    JsonValue systemValue = jsonObject.get(PROPERTY_SYSTEM_NAME);
    System system = System.ROUND_ROBIN;
    if (!JsonValue.NULL.equals(systemValue) && systemValue != null) {
      String systemName = get(jsonObject, PROPERTY_SYSTEM_NAME);
      system = System.valueOf(systemName);
    }
    round.setSystem(system);

    return round;
  }

  @Override
  public Collection<Round> toModels(JsonArray jsonArray, Collection<Round> rounds) {
    jsonArray.forEach(item -> {
      JsonObject jsonObject = (JsonObject) item;
      Round round = getProperEntity(jsonObject, rounds);
      toModel(jsonObject, round);
    });

    return rounds;
  }
}
