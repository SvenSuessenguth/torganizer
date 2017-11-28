package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.json.JsonObjectBuilder;

/**
 * this class contains helper methods to create and parse jsondata.
 */
public interface AbstractJsonConverter {

  default void addWithEmptyDefault(JsonObjectBuilder objectBuilder, String name, Object value) {
    addWithDefault(objectBuilder, name, value, "");
  } 
  
  default void addWithDefault(JsonObjectBuilder objectBuilder, String name, Object value, Object defaultValue) {
    if (value == null) {
      objectBuilder.add(name, defaultValue.toString());
    } else {
      objectBuilder.add(name, value.toString());
    }
  }

  default String localDateTimeToString(LocalDateTime ldt) {
    return "";
  }

  default LocalDateTime localDateTimeFromString(String string) {
    return null;
  }

  default String localDateToString(LocalDate localDate) {
    return localDate != null ? localDate.format(DateTimeFormatter.ISO_DATE) : "";
  }

  default LocalDate localDateFromString(String string) {
    LocalDate localDate = null;

    if (string == null || Objects.equals("", string)) {
      localDate = null;
    } else {
      localDate = LocalDate.parse(string, DateTimeFormatter.ISO_DATE);
    }

    return localDate;
  }

  default Long longFromString(String string) {
    Long value;
    if (string == null || Objects.equals("", string)) {
      value = null;
    } else {
      value = Long.valueOf(string);
    }

    return value;
  }
}
