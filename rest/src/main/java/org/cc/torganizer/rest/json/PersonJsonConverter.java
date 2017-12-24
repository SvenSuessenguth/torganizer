/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;

/**
 *
 * @author svens
 */
@RequestScoped
public class PersonJsonConverter extends ModelJsonConverter<Person>{

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
  public Person toModel(JsonObject jsonObject) {
    
    String firstName = jsonObject.getString("firstName");
    String lastName = jsonObject.getString("lastName");
    Person person = new Person(firstName, lastName);

    String idString = get(jsonObject, "id");
    Long id = idString==null?null:Long.valueOf(idString);
    person.setId(id);
    
    String genderString = get(jsonObject, "gender");
    Gender gender = Gender.valueOf(genderString);
    person.setGender(gender);
    
    String dateOfBirthString = get(jsonObject, "dateOfBirth");
    LocalDate dateOfBirth = dateOfBirthString==null?null:LocalDate.parse(dateOfBirthString, DateTimeFormatter.ISO_DATE);
    person.setDateOfBirth(dateOfBirth);
    
    return person;
  }

  @Override
  public Collection<Person> toModels(JsonArray jsonArray) {
    List<Person> persons = new ArrayList<>();
    
    jsonArray.forEach((JsonValue arrayValue) -> persons.add(toModel((JsonObject)arrayValue)));
    
    return persons;
  }
}
