package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.*;
import java.util.Collection;
import java.util.HashMap;

@RequestScoped
public class GroupJsonConverter extends BaseModelJsonConverter<Group> {

  private static final JsonArray EMPTY_OPPONENT_ARRAY = Json.createBuilderFactory(new HashMap<>())
    .createArrayBuilder().build();

  @Inject
  private OpponentJsonConverterProvider opponentJsonConverterProvider;

  @Override
  public JsonObject toJsonObject(Group group) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    add(objectBuilder, "id", group.getId());
    add(objectBuilder, "position", group.getPosition());

    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Group> groups) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    groups.forEach(group -> arrayBuilder.add(this.toJsonObject(group)));

    return arrayBuilder.build();
  }

  @Override
  public Group toModel(JsonObject jsonObject, Group group) {
    JsonValue positionValue = jsonObject.get("position");
    Integer position = null;
    if (!JsonValue.NULL.equals(positionValue)) {
      position = Integer.valueOf(positionValue.toString());
    }
    group.setPosition(position);

    return group;
  }

  @Override
  public Collection<Group> toModels(JsonArray jsonArray, Collection<Group> groups) {
    jsonArray.forEach(item -> {
      JsonObject jsonObject = (JsonObject) item;
      Group group = getProperEntity(jsonObject, groups);
      toModel(jsonObject, group);
    });

    return groups;
  }

  /**
   * Add the Oppontens from the group converted as json to the groupJson.
   */
  public JsonObject addOpponents(JsonObject groupJson, Collection<Opponent> opponents) {
    BaseModelJsonConverter<Opponent> converter = opponentJsonConverterProvider
      .getConverter(opponents);
    JsonArray opponentsJsonArray = converter == null
      ? EMPTY_OPPONENT_ARRAY
      : converter.toJsonArray(opponents);

    JsonPatch patch = Json.createPatchBuilder()
      .add("/opponents", opponentsJsonArray)
      .build();

    return patch.apply(groupJson);
  }
}
