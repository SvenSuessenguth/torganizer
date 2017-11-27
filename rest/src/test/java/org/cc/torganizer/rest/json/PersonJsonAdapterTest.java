package org.cc.torganizer.rest.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.LocalDate;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.junit.Before;
import org.junit.Test;

public class PersonJsonAdapterTest {

  private Jsonb jsonb;
  
  @Before
  public void before() {
    // object to test
    PersonJsonAdapter adapter = new PersonJsonAdapter();
    
    JsonbConfig jc = new JsonbConfig().withAdapters(adapter);
    jsonb = JsonbBuilder.create(jc);
  }
  
  @Test
  public void testAdaptToJson_allFieldsAreSet() throws Exception {
    Person p = new Person("vorname", "nachname");
    p.setGender(Gender.MALE);
    p.setDateOfBirth(LocalDate.now());
    p.setId(1L);
    
    String json = jsonb.toJson(p);
    
    String expected = "{\"id\":\"1\",\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"gender\":\"MALE\",\"dateOfBirth\":\"2017-11-27\"}";
    assertThat(json, is(expected));
  }
  
  @Test
  public void testAdaptToJson_DateOfBirthIsNull() throws Exception {
    Person p = new Person("vorname", "nachname");
    p.setGender(Gender.MALE);
    p.setDateOfBirth(null);
    p.setId(1L);
    
    String json = jsonb.toJson(p);
    
    String expected = "{\"id\":\"1\",\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"gender\":\"MALE\",\"dateOfBirth\":\"\"}";
    assertThat(json, is(expected));
  }
  
  @Test
  public void testAdaptToJson_idIsNull() throws Exception {
    Person p = new Person("vorname", "nachname");
    p.setGender(Gender.MALE);
    p.setDateOfBirth(LocalDate.now());
    p.setId(null);
    
    String json = jsonb.toJson(p);
    
    String expected = "{\"id\":\"\",\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"gender\":\"MALE\",\"dateOfBirth\":\"2017-11-27\"}";
    assertThat(json, is(expected));
  }
  
  @Test
  public void testAdaptToPerson_allFieldsAreSet() {
    String json = "{\"id\":\"1\",\"firstName\":\"vorname\",\"lastName\":\"nachname\",\"gender\":\"MALE\",\"dateOfBirth\":\"2017-11-27\"}";
    
    Person person = jsonb.fromJson(json, Person.class);
    
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
