package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Entity;

import javax.json.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

public abstract class ModelJsonConverter<T extends Entity> {
  
  public abstract JsonObject toJsonObject(T t);
  public abstract JsonArray toJsonArray(Collection<T> ts);
  public abstract T toModel(JsonObject jsonObject, T model);
  public abstract Collection<T> toModels(JsonArray jsonArray, Collection<T> models);

  // use existing object (update)
  public T getProperEntity(JsonObject jsonObject, Collection<T> models){
    Long id = Long.valueOf(jsonObject.get("id").toString());
    for(T model : models){
      if(Objects.equals(model.getId(),id)){
        return model;
      }
    }

    // use new Object (create)
    try{
      Type sooper = getClass().getGenericSuperclass();
      Type t = ((ParameterizedType)sooper).getActualTypeArguments()[ 0 ];

      Class tClass = Class.forName(t.getTypeName());

      return (T)tClass.getConstructor().newInstance();
    }catch(ClassNotFoundException|NoSuchMethodException|InstantiationException|InvocationTargetException|IllegalAccessException exc ){
      throw new ModelJsonConverterException(exc);
    }
  }

  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, JsonObject value){
    if(value==null){
      return objectBuilder.add(name, JsonValue.NULL);
    }else {
      return objectBuilder.add(name, value);
    }
  }
  
  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, LocalDate value){
    if(value==null){
      return objectBuilder.add(name, JsonValue.NULL);
    }else {
      return objectBuilder.add(name, localDateToString(value));
    }
  }
  protected String localDateToString(LocalDate localDate){
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
    return localDate.format(formatter);
  }
  
  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, LocalDateTime value){
    if(value==null){
      return objectBuilder.add(name, JsonValue.NULL);
    }else {
      return objectBuilder.add(name, localDateTimeToString(value));
    }
  }
  protected String localDateTimeToString(LocalDateTime localDateTime){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return localDateTime.format(formatter);
  }
  protected LocalDateTime localDateTimeFromString(String localDateTimeString){
    if(localDateTimeString==null||localDateTimeString.isEmpty()){
      return null;
    }
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(localDateTimeString, formatter);
  }
  
  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, Long value){
    if(value==null){
      return objectBuilder.add(name, JsonValue.NULL);
    }else {
      return objectBuilder.add(name, value);
    }
  }

  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, Integer integer){
    if(integer==null){
      return objectBuilder.add(name, JsonValue.NULL);
    }else {
      return objectBuilder.add(name, integer);
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
      
      if(jsonValue.getValueType().equals(JsonValue.ValueType.STRING)){
        return ((JsonString)jsonValue).getString();
      }
      
      if(jsonValue.getValueType().equals(JsonValue.ValueType.NUMBER)){
        int number = ((JsonNumber) jsonValue).intValue();
        return Integer.toString(number);
      }
    }
    
    return null;
  }

  public static final JsonArray emptyArray(){
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    return arrayBuilder.build();
  }
}
