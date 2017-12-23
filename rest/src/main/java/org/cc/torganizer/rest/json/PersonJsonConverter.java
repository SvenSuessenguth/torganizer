/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import java.util.Collection;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.cc.torganizer.core.entities.Person;

/**
 *
 * @author svens
 */
public class PersonJsonConverter extends ModelJsonConverter<Person>{

  @Override
  public JsonObject toJsonObject(Person person) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<String, Object>());
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
    arrayBuilder.add("persons");
    
    persons.forEach((person) -> {
      arrayBuilder.add(this.toJsonObject(person));
    });
    
    return arrayBuilder.build();
  }
  
  @Override
  public Person toModel(JsonObject jsonObject) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<Person> toModels(JsonArray jsonArray) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  
}
