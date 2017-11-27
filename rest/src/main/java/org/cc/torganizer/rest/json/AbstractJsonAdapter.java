package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.json.JsonObjectBuilder;
import javax.json.bind.adapter.JsonbAdapter;

/**
 * this class contains helper methods to create and parse jsondata.
 */
public abstract class AbstractJsonAdapter<T, V> implements JsonbAdapter<T, V> {

  public void addWithDefault(JsonObjectBuilder objectBuilder, String name, Object value, String defaultValue) {
    if (value == null) {
      objectBuilder.add(name, defaultValue);
    } else {
      objectBuilder.add(name, value.toString());
    }
  }

  public String toString(LocalDateTime ldt) {
    return "";
  }

  public LocalDateTime toLocalDateTime(String string) {
    return null;
  }

  public String toISO(LocalDate localDate) {
    return localDate != null ? localDate.format(DateTimeFormatter.ISO_DATE) : "";
  }

  public LocalDate fromISO(String string) {
    LocalDate localDate = null;

    if (string == null || Objects.equals("", string)) {
      localDate = null;
    } else {
      localDate = LocalDate.parse(string, DateTimeFormatter.ISO_DATE);
    }

    return localDate;
  }

  public Long toLong(String string) {
    Long value;
    if (string == null || Objects.equals("", string)) {
      value = null;
    } else {
      value = Long.valueOf(string);
    }

    return value;
  }
}
