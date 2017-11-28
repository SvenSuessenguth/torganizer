package org.cc.torganizer.rest.json;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.bind.adapter.JsonbAdapter;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

public class PersonJsonAdapter implements AbstractJsonAdapter, JsonbAdapter<Person, JsonObject> {

  @Override
  public JsonObject adaptToJson(Person person) throws Exception {
    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

    addWithEmptyDefault(objectBuilder, "id", person.getId());
    addWithEmptyDefault(objectBuilder, "firstName", person.getFirstName());
    addWithEmptyDefault(objectBuilder, "lastName", person.getLastName());
    addWithDefault(objectBuilder, "gender", person.getGender(), Gender.UNKNOWN);
    addWithEmptyDefault(objectBuilder, "dateOfBirth", localDateToString(person.getDateOfBirth()));

    return objectBuilder.build();
  }

  @Override
  public Person adaptFromJson(JsonObject jo) throws Exception {
    Person person = new Person();

    person.setFirstName(jo.getString("firstName", null));
    person.setLastName(jo.getString("lastName", null));
    person.setDateOfBirth(localDateFromString(jo.getString("dateOfBirth", null)));
    person.setId(longFromString(jo.getString("id", null)));
    person.setGender(Gender.valueOf(jo.getString("gender", null)));

    return person;
  }

}
