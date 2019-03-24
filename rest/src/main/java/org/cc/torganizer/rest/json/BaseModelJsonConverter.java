package org.cc.torganizer.rest.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.cc.torganizer.core.entities.Entity;

public abstract class BaseModelJsonConverter<T extends Entity> implements ModelJsonConverter<T> {

  // use existing object (update)
  @SuppressWarnings("unchecked")
  public T getProperEntity(JsonObject jsonObject, Collection<T> models) {
    Long id = Long.valueOf(jsonObject.get("id").toString());
    for (T model : models) {
      if (Objects.equals(model.getId(), id)) {
        return model;
      }
    }

    // use new Object (create)
    try {
      Type sooper = getClass().getGenericSuperclass();
      Type t = ((ParameterizedType) sooper).getActualTypeArguments()[0];

      Class<?> typeClass = Class.forName(t.getTypeName());

      return (T) typeClass.getConstructor().newInstance();
    } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
        | InvocationTargetException | IllegalAccessException exc) {
      throw new ModelJsonConverterException(exc);
    }
  }

  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, JsonObject value) {
    if (value == null) {
      return objectBuilder.add(name, JsonValue.NULL);
    } else {
      return objectBuilder.add(name, value);
    }
  }

  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, LocalDate value) {
    if (value == null) {
      return objectBuilder.add(name, JsonValue.NULL);
    } else {
      return objectBuilder.add(name, localDateToString(value));
    }
  }

  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, LocalDateTime value) {
    if (value == null) {
      return objectBuilder.add(name, JsonValue.NULL);
    } else {
      return objectBuilder.add(name, localDateTimeToString(value));
    }
  }

  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, Long value) {
    if (value == null) {
      return objectBuilder.add(name, JsonValue.NULL);
    } else {
      return objectBuilder.add(name, value);
    }
  }

  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, Integer integer) {
    if (integer == null) {
      return objectBuilder.add(name, JsonValue.NULL);
    } else {
      return objectBuilder.add(name, integer);
    }
  }

  public JsonObjectBuilder add(JsonObjectBuilder objectBuilder, String name, String value) {
    if (value == null) {
      return objectBuilder.add(name, JsonValue.NULL);
    } else {
      return objectBuilder.add(name, value);
    }
  }

  protected String localDateToString(LocalDate localDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
    return localDate.format(formatter);
  }

  protected String localDateTimeToString(LocalDateTime localDateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return localDateTime.format(formatter);
  }

  protected LocalDateTime localDateTimeFromString(String localDateTimeString) {
    if (localDateTimeString == null || localDateTimeString.isEmpty()) {
      return null;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(localDateTimeString, formatter);
  }

  public String get(JsonObject jsonObject, String key) {
    if (jsonObject.containsKey(key)) {
      JsonValue jsonValue = jsonObject.get(key);

      if (JsonValue.NULL.equals(jsonValue)) {
        return null;
      }

      if (jsonValue.getValueType().equals(JsonValue.ValueType.STRING)) {
        return ((JsonString) jsonValue).getString();
      }

      if (jsonValue.getValueType().equals(JsonValue.ValueType.NUMBER)) {
        int number = ((JsonNumber) jsonValue).intValue();
        return Integer.toString(number);
      }
    }

    return null;
  }

  public static final JsonArray emptyArray() {
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    return arrayBuilder.build();
  }
}
