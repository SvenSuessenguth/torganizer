package org.cc.torganizer.rest.json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

public class PersonJsonConverter implements AbstractJsonConverter {

  public JsonObject toJson(Person person) throws Exception {
    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

    addWithEmptyDefault(objectBuilder, "id", person.getId());
    addWithEmptyDefault(objectBuilder, "firstName", person.getFirstName());
    addWithEmptyDefault(objectBuilder, "lastName", person.getLastName());
    addWithDefault(objectBuilder, "gender", person.getGender(), Gender.UNKNOWN);
    addWithEmptyDefault(objectBuilder, "dateOfBirth", localDateToString(person.getDateOfBirth()));

    return objectBuilder.build();
  }

  public Person fromJson(JsonObject jo) throws Exception {
    Person person = new Person();

    person.setFirstName(jo.getString("firstName", null));
    person.setLastName(jo.getString("lastName", null));
    person.setDateOfBirth(localDateFromString(jo.getString("dateOfBirth", null)));
    person.setId(longFromString(jo.getString("id", null)));
    person.setGender(Gender.valueOf(jo.getString("gender", null)));

    return person;
  }

}
