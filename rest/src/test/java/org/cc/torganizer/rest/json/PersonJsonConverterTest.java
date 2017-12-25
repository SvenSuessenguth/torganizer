/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
    
    assertThat(result, is(notNullValue()));
    assertThat(result.toString(), is(expected));
  }
  
  @Test
  public void testToModelObject_withNullValues() {
    String jsonString = "{\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();     
    
    Person person = converter.toModel(jsonObject);
    
    assertThat(person.getDateOfBirth(), is(nullValue()));
    assertThat(person.getGender(), is(Gender.UNKNOWN));
    assertThat(person.getFirstName(), is("vorname"));
  }
  
  @Test
  public void testToModel_withId(){
    String jsonString = "{\"id\":0,\"firstName\":\"A\",\"lastName\":\"A\",\"dateOfBirth\":\"2017-12-25\",\"gender\":\"MALE\"}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();     
    
    Person person = converter.toModel(jsonObject);
    
    assertThat(person.getId(), is(0L));
    assertThat(person.getDateOfBirth(), is(LocalDate.of(2017, Month.DECEMBER, 25)));
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
    
    assertThat(persons, is(notNullValue()));
    assertThat(persons.size(), is(2));
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
