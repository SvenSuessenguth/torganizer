package org.cc.torganizer.rest.json;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

public class PersonJsonConverter extends AbstractJsonConverter implements JsonConverter<Person> {

  /**
   * <code>
   * {"firstName":"vorname","lastName":"nachname","dateOfBirth":"2017-11-25","gender":"MALE","id":1}
   * </code>
   */
  @Override
  public JsonObject toJson(Person person) {
    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

    objectBuilder.add("firstName", person.getFirstName()).add("lastName", person.getLastName())
        .add("dateOfBirth", person.getDateOfBirthISO()).add("gender", person.getGender().toString());

    addIfNotNull(objectBuilder, "id", person.getId());
    
    return objectBuilder.build();
  }

  /**
   * <code>
   * {"persons":[
   *     {"firstName":"vorname-0","lastName":"nachname-0","dateOfBirth":"2017-11-25","gender":"MALE","id":1},
   *     {"firstName":"vorname-1","lastName":"nachname-1","dateOfBirth":"2017-11-25","gender":"MALE","id":1},
   *     {"firstName":"vorname-2","lastName":"nachname-2","dateOfBirth":"2017-11-25","gender":"MALE","id":1}
   *   ]
   * }
   * </code>
   */
  @Override
  public JsonObject toJson(List<Person> persons) {

    JsonArrayBuilder jab = Json.createArrayBuilder();
    for (Person person : persons) {
      jab.add(toJson(person));
    }
    JsonArray array = jab.build();

    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
    objectBuilder.add("persons", array);

    return objectBuilder.build();
  }

  @Override
  public Person toObject(JsonObject jsonObject) {
    Person person = new Person();

    
    Long id = getLong(jsonObject, "id");
    person.setId(id);
    person.setFirstName(jsonObject.getString("firstName"));
    person.setLastName(jsonObject.getString("lastName"));
    person.setGender(Gender.valueOf(jsonObject.getString("gender")));
    person.setDateOfBirthISO(jsonObject.getString("dateOfBirth"));

    return person;
  }

  @Override
  public List<Person> toList(JsonObject jsonObject) {
    List<Person> persons = new ArrayList<>();

    JsonValue jsonValue = jsonObject.get("persons");
    JsonArray jsonArray = jsonValue.asJsonArray();
    for (JsonValue jv : jsonArray) {
      JsonObject jo = jv.asJsonObject();
      Person p = toObject(jo);
      persons.add(p);
    }

    return persons;
  }
}
