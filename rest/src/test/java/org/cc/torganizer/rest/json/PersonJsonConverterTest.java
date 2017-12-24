/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author svens
 */
public class PersonJsonConverterTest {
  
  private PersonJsonConverter converter;
  
  @Before
  public void setUp() {
    converter = new PersonJsonConverter();
  }
  
  @Test
  public void testToJsonObject_withNullValues() {
    String expected = "{\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}";
    
    // id und dateOfBirth sind null
    // gender kann nicht null sein
    Person person = new Person("vorname", "nachname");
    
    JsonObject result = converter.toJsonObject(person);
    
    MatcherAssert.assertThat(result, Matchers.is(Matchers.notNullValue()));
    MatcherAssert.assertThat(result.toString(), Matchers.is(expected));
  }
  
  @Test
  public void testToModelObject_withNullValues() {
    String jsonString = "{\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();     
    
    Person person = converter.toModel(jsonObject);
    
    MatcherAssert.assertThat(person.getDateOfBirth(), Matchers.is(Matchers.nullValue()));
    MatcherAssert.assertThat(person.getGender(), Matchers.is(Gender.UNKNOWN));
    MatcherAssert.assertThat(person.getFirstName(), Matchers.is("vorname"));
  }
  
  @Test
  public void testToModelObjects() {
    String jsonString = "["
            + "{\"id\":null,\"firstName\":\"vornameA\",\"lastName\":\"nachnameA\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
            + "{\"id\":null,\"firstName\":\"vornameB\",\"lastName\":\"nachnameB\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}"
            + "]";
    
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonArray jsonArray = jsonReader.readArray();
    
    
    Collection<Person> persons = converter.toModels(jsonArray);
    
    MatcherAssert.assertThat(persons, Matchers.is(Matchers.notNullValue()));
    MatcherAssert.assertThat(persons.size(), Matchers.is(2));
  }
  
  @Test
  public void testToJsonObject_multiplePersons() {
    String expected = "["
            + "{\"id\":null,\"firstName\":\"vornameA\",\"lastName\":\"nachnameA\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
            + "{\"id\":null,\"firstName\":\"vornameB\",\"lastName\":\"nachnameB\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}"
            + "]";
    
    // id und dateOfBirth sind null
    // gender kann nicht null sein
    Person personA = new Person("vornameA", "nachnameA");
    Person personB = new Person("vornameB", "nachnameB");
    List<Person> persons = Arrays.asList(personA, personB);
    
    final JsonArray result = converter.toJsonArray(persons);
    
    assertThat(result, is(notNullValue()));
    assertThat(result.toString(), is(expected));
  }
}
