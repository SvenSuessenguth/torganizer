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

import org.cc.torganizer.core.entities.Group;

@RequestScoped
public class GroupJsonConverter extends BaseModelJsonConverter<Group> {

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
}
