package org.cc.torganizer.rest.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.rest.json.PersonJsonConverter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class PersonJsonConverterTest {

  private PersonJsonConverter converter;
  
  @Before
  public void before() {
    converter = new PersonJsonConverter();
  }
  
  @Test
  public void testToJsonAndBack() throws Exception {
    LocalDate dateOfBirth = LocalDate.now();
    
    Person p = new Person("vorname", "nachname");
    p.setGender(Gender.MALE);
    p.setDateOfBirth(dateOfBirth);
    p.setId(1L);
    
    JsonObject jsonObject = converter.toJson(p);
    
    Person p_ = converter.toObject(jsonObject);
    
    assertThat(p_.getId(), is(1L));
    assertThat(p_.getGender(), is(Gender.MALE));
    assertThat(p_.getFirstName(), is("vorname"));
    assertThat(p_.getLastName(), is("nachname"));
    assertThat(p_.getDateOfBirth(), is(dateOfBirth));
  }
  
  @Test
  public void testToJsonAndBackList() throws Exception {
    LocalDate dateOfBirth = LocalDate.now();
    List<Person> persons = new ArrayList<>();
    
    for(int i=0; i<10 ; i++) {
      Person p = new Person("vorname-"+i, "nachname-"+i);
      p.setGender(Gender.MALE);
      p.setDateOfBirth(dateOfBirth);
      p.setId(1L);  
      
      persons.add(p);
    }
    
    JsonObject jsonObject = converter.toJson(persons);
    List<Person> persons_ = converter.toList(jsonObject);
    
    assertThat(persons_, Matchers.hasSize(10));
  }
}
