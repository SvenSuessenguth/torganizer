package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Entity;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Collection;

public interface ModelJsonConverter<T extends Entity> {
  JsonObject toJsonObject(T t);

  JsonArray toJsonArray(Collection<T> ts);

  T toModel(JsonObject jsonObject, T model);

  Collection<T> toModels(JsonArray jsonArray, Collection<T> models);
}
