/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import java.util.Arrays;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.cc.torganizer.core.entities.Person;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
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
  public void testToJsonObject_multiplePersons() {
    String expected = "[\"persons\","
            + "{\"id\":null,\"firstName\":\"vornameA\",\"lastName\":\"nachnameA\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
            + "{\"id\":null,\"firstName\":\"vornameB\",\"lastName\":\"nachnameB\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}"
            + "]";
    
    // id und dateOfBirth sind null
    // gender kann nicht null sein
    Person personA = new Person("vornameA", "nachnameA");
    Person personB = new Person("vornameB", "nachnameB");
    List<Person> persons = Arrays.asList(personA, personB);
    
    final JsonArray result = converter.toJsonArray(persons);
    
    MatcherAssert.assertThat(result, Matchers.is(Matchers.notNullValue()));
    MatcherAssert.assertThat(result.toString(), Matchers.is(expected));
  }
}
