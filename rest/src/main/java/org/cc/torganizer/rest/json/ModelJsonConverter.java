package org.cc.torganizer.rest.json;

import java.util.Collection;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.cc.torganizer.core.entities.Entity;

public interface ModelJsonConverter<T extends Entity> {
  JsonObject toJsonObject(T t);

  JsonArray toJsonArray(Collection<T> ts);

  T toModel(JsonObject jsonObject, T model);

  Collection<T> toModels(JsonArray jsonArray, Collection<T> models);
}
