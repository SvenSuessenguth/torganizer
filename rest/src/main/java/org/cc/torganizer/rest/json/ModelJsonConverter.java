package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

public abstract class ModelJsonConverter<T> {
  
  public abstract JsonObject toJsonObject(T t);
  public abstract JsonArray toJsonArray(Collection<T> ts);
  public abstract T toModel(JsonObject jsonObject);
  public abstract Collection<T> toModels(JsonArray jsonArray);
  
  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, LocalDate value){
    if(value==null){
      return objectBuilder.add(name, JsonValue.NULL);
    }else {
      return objectBuilder.add(name, value.format(DateTimeFormatter.ISO_DATE));
    }
  }
  
  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, Long value){
    if(value==null){
      return objectBuilder.add(name, JsonValue.NULL);
    }else {
      return objectBuilder.add(name, value);
    }
  }
  
  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, String value){
    if(value==null){
      return objectBuilder.add(name, JsonValue.NULL);
    }else {
      return objectBuilder.add(name, value);
    }
  }
  
  public String get(JsonObject jsonObject, String key){
    if(jsonObject.containsKey(key)){
      JsonValue jsonValue = jsonObject.get(key);
      if(JsonValue.NULL.equals(jsonValue)){
        return null;
      }
      return ((JsonString)jsonValue).getString();
    }else{
      return null;
    }
  }
}
