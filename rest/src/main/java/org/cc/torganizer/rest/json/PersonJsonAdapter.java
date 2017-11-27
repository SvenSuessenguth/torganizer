package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

public class PersonJsonAdapter implements JsonbAdapter<Person, JsonObject> {

  private String toISO(LocalDate localDate) {
    return localDate != null ? localDate.format(DateTimeFormatter.ISO_DATE) : "";
  }

  private LocalDate fromISO(String string) {
    LocalDate localDate = null;

    if (string == null || Objects.equals("", string)) {
      localDate = null;
    } else {
      localDate = LocalDate.parse(string, DateTimeFormatter.ISO_DATE);
    }

    return localDate;
  }

  private Long toLong(String string) {
    Long value;
    if (string == null || Objects.equals("", string)) {
      value = null;
    } else {
      value = Long.valueOf(string);
    }

    return value;
  }

  @Override
  public JsonObject adaptToJson(Person person) throws Exception {
    return Json.createObjectBuilder()
        .add("id", person.getId() == null ? "" : "" + person.getId())
        .add("firstName", person.getFirstName())
        .add("lastName", person.getLastName())
        .add("gender", person.getGender().toString())
        .add("dateOfBirth", toISO(person.getDateOfBirth())).build();
  }

  @Override
  public Person adaptFromJson(JsonObject jo) throws Exception {
    Person person = new Person();

    person.setFirstName(jo.getString("firstName", null));
    person.setLastName(jo.getString("lastName", null));
    person.setDateOfBirth(fromISO(jo.getString("dateOfBirth", null)));
    person.setId(toLong(jo.getString("id")));
    person.setGender(Gender.valueOf(jo.getString("gender", null)));

    return person;
  }

}
