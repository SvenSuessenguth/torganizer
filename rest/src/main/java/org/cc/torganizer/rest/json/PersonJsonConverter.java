package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

/**
 * @author svens
 */
@RequestScoped
public class PersonJsonConverter extends BaseModelJsonConverter<Person> {

  @Override
  public JsonObject toJsonObject(Person person) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    add(objectBuilder, "id", person.getId());
    add(objectBuilder, "firstName", person.getFirstName());
    add(objectBuilder, "lastName", person.getLastName());
    add(objectBuilder, "dateOfBirth", person.getDateOfBirth());
    add(objectBuilder, "gender", person.getGender().toString());

    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Person> persons) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    persons.forEach(person -> arrayBuilder.add(this.toJsonObject(person)));

    return arrayBuilder.build();
  }

  @Override
  public Person toModel(JsonObject jsonObject, Person person) {
    String firstName = jsonObject.getString("firstName");
    String lastName = jsonObject.getString("lastName");
    person.setFirstName(firstName);
    person.setLastName(lastName);

    String genderString = get(jsonObject, "gender");
    Gender gender = Gender.valueOf(genderString);
    person.setGender(gender);

    String dateOfBirthString = get(jsonObject, "dateOfBirth");
    LocalDate dateOfBirth = dateOfBirthString == null || dateOfBirthString.trim().isEmpty()
        ? null
        : LocalDate.parse(dateOfBirthString, DateTimeFormatter.ISO_DATE);
    person.setDateOfBirth(dateOfBirth);

    return person;
  }

  @Override
  public Collection<Person> toModels(JsonArray jsonArray, Collection<Person> persons) {
    jsonArray.forEach(item -> {
      JsonObject jsonObject = (JsonObject) item;
      Person person = getProperEntity(jsonObject, persons);
      toModel(jsonObject, person);
    });

    return persons;
  }
}
