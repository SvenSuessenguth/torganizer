package org.cc.torganizer.rest.json;

import java.util.Objects;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public abstract class AbstractJsonConverter {

  /**
   * Aufbau eines JSON-Stringg. 
   */
  public void addIfNotNull(JsonObjectBuilder builder, String name, Object value) {
    if (value != null) {
      builder.add(name, value.toString());
    }
  }

  public Long getLong(JsonObject jo, String name) {
    Long l = null;

    Object obj = jo.get(name);
    if (obj != null && !Objects.equals("", obj)) {      
      String value = removeParanthesis(obj.toString());
      
      l = Long.valueOf(value);
    }

    return l;
  }

  private String removeParanthesis(String string) {
    return string.substring(1, string.length()-1);
  }
}
