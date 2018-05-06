package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Round;

import javax.enterprise.context.RequestScoped;
import javax.json.*;
import java.util.Collection;
import java.util.HashMap;

/**
 * A json-disciplines contains nothing but id, name and restrictions.
 *
 */
@RequestScoped
public class RoundJsonConverter extends ModelJsonConverter<Round> {

  @Override
  public JsonObject toJsonObject(Round round) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    add(objectBuilder, "id", round.getId());
    add(objectBuilder, "qualified", round.getQualified());
    add(objectBuilder, "position", round.getPosition());

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
    if(qualified==null){
      qualified = 0;
    }
    round.setQualified(qualified);

    JsonValue positionValue = jsonObject.get("position");
    Integer position = null;
    if(!JsonValue.NULL.equals(positionValue) && positionValue!=null){
      position = Integer.valueOf(positionValue.toString());
    }
    round.setPosition(position);

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
