package org.cc.torganizer.rest.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.StringReader;
import java.time.LocalDate;

import javax.json.Json;
import javax.json.JsonObject;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.junit.Before;
import org.junit.Test;

public class PersonJsonConverterTest {

  private PersonJsonConverter converter;
  
  @Before
  public void before() {
    // object to test
     converter = new PersonJsonConverter();

  }

  @Test
  public void testAdaptToJson_allFieldsAreSet() throws Exception {
    Person p = new Person("vorname", "nachname");
    p.setGender(Gender.MALE);
    p.setDateOfBirth(LocalDate.of(2017, 11, 27));
    p.setId(1L);

    String json = converter.toJson(p).toString();

    String expected = "{\"id\":\"1\",\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"gender\":\"MALE\",\"dateOfBirth\":\"2017-11-27\"}";
    assertThat(json, is(expected));
  }

  @Test
  public void testAdaptToJson_DateOfBirthIsNull() throws Exception {
    Person p = new Person("vorname", "nachname");
    p.setGender(Gender.MALE);
    p.setDateOfBirth(null);
    p.setId(1L);

    String json = converter.toJson(p).toString();

    String expected = "{\"id\":\"1\",\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"gender\":\"MALE\",\"dateOfBirth\":\"\"}";
    assertThat(json, is(expected));
  }

  @Test
  public void testAdaptToJson_idIsNull() throws Exception {
    Person p = new Person("vorname", "nachname");
    p.setGender(Gender.MALE);
    p.setDateOfBirth(LocalDate.of(2017, 11, 27));
    p.setId(null);

    String json = converter.toJson(p).toString();

    String expected = "{\"id\":\"\",\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"gender\":\"MALE\",\"dateOfBirth\":\"2017-11-27\"}";
    assertThat(json, is(expected));
  }

  @Test
  public void testAdaptToPerson_allFieldsAreSet() throws Exception {
    String jsonString = "{\"id\":\"1\",\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"gender\":\"MALE\",\"dateOfBirth\":\"2017-11-27\"}";
    StringReader sr = new StringReader(jsonString);
    
    JsonObject json = Json.createReader(sr).readObject();

    Person person = converter.fromJson(json);

    assertThat(person, is(notNullValue()));
    assertThat(person.getId(), is(1L));
    assertThat(person.getFirstName(), is("vorname"));
    assertThat(person.getLastName(), is("nachname"));
    assertThat(person.getGender(), is(Gender.MALE));
    assertThat(person.getDateOfBirth().getYear(), is(2017));
    assertThat(person.getDateOfBirth().getMonthValue(), is(11));
    assertThat(person.getDateOfBirth().getDayOfMonth(), is(27));
  }

}
