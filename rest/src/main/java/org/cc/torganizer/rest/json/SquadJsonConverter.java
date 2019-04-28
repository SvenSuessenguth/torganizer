package org.cc.torganizer.rest.json;

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

import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.Squad;

@RequestScoped
public class SquadJsonConverter extends BaseModelJsonConverter<Squad>
    implements OpponentJsonConverter {

  @Inject
  private PlayerJsonConverter playerConverter;

  public SquadJsonConverter() {
  }

  public SquadJsonConverter(PlayerJsonConverter playerConverter) {
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
  public JsonArray toJsonArray(Collection squads) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    squads.forEach(squad -> arrayBuilder.add(this.toJsonObject((Squad) squad)));

    return arrayBuilder.build();
  }

  @Override
  public Squad toModel(JsonObject jsonObject, Squad squad) {
    return squad;
  }

  @Override
  public Collection<Squad> toModels(JsonArray jsonArray, Collection squads) {
    return squads;
  }

  @Override
  public OpponentType getOpponentType() {
    return OpponentType.SQUAD;
  }
}
