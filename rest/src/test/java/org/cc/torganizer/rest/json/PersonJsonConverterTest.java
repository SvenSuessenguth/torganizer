/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import static java.time.Month.DECEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cc.torganizer.core.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author svens
 */
class PersonJsonConverterTest {

  private PersonJsonConverter converter;

  @BeforeEach
  void setUp() {
    converter = new PersonJsonConverter();
  }

  @Test
  void testToJsonObject_withNullValues() {
    String expected = "{\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}";

    // id und dateOfBirth sind null
    // gender kann nicht null sein
    Person person = new Person("vorname", "nachname");

    JsonObject result = converter.toJsonObject(person);

    assertThat(result).isNotNull();
    assertThat(result.toString()).hasToString(expected);
  }

  @Test
  void testToModelObject_withNullValues() {
    String jsonString = "{\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
        + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Person person = converter.toModel(jsonObject, new Person());

    assertThat(person.getDateOfBirth()).isNull();
    assertThat(person.getGender()).isEqualTo(UNKNOWN);
    assertThat(person.getFirstName()).isEqualTo("vorname");
  }

  @Test
  void testToModel_withId() {
    String jsonString = "{\"id\":0,\"firstName\":\"A\",\"lastName\":\"A\",\"dateOfBirth\":\"2017-12-25\",\"gender\":\"MALE\"}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Person person = converter.toModel(jsonObject, new Person(0L));

    assertThat(person.getId()).isEqualTo(0L);
    assertThat(person.getDateOfBirth()).isEqualTo(LocalDate.of(2017, DECEMBER, 25));
  }

  @Test
  void testToModelObjects() {
    String jsonString = "["
        + "{\"id\":1,\"firstName\":\"vornameA\",\"lastName\":\"nachnameA\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
        + "{\"id\":2,\"firstName\":\"vornameB\",\"lastName\":\"nachnameB\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}"
        + "]";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonArray jsonArray = jsonReader.readArray();
    Collection<Person> persons = Arrays.asList(new Person(1L), new Person(2L));


    persons = converter.toModels(jsonArray, persons);

    assertThat(persons).hasSize(2);
  }

  @Test
  void testToJsonObject_multiplePersons() {
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

    assertThat(result).isNotNull();
    assertThat(result.toString()).hasToString(expected);
  }
}
