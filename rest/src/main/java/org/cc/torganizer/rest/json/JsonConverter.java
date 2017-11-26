package org.cc.torganizer.rest.json;

import java.util.List;

import javax.json.JsonObject;

public interface JsonConverter<T> {
  JsonObject toJson(T object);
  
  /**
   * @see https://stackoverflow.com/questions/18983185/how-to-create-correct-jsonarray-in-java-using-jsonobject
   */
  JsonObject toJson(List<T> objects);
  
  T toObject(JsonObject jsonObject);
  List<T> toList(JsonObject jsonObject);
}
