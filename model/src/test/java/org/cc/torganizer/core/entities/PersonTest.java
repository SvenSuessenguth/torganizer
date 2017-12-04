/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.core.entities;

import java.time.LocalDate;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 *
 * @author u000349
 */
public class PersonTest { 
    
  @Test
  public void testJson(){
    String expectedJson = "{\"dateOfBirth\":\"2000-01-01\",\"firstName\":\"firstName\",\"gender\":\"UNKNOWN\",\"lastName\":\"lastName\"}";
    Person person = new Person("firstName", "lastName");
    person.setDateOfBirth(LocalDate.of(2000, 1, 1));
    
    Jsonb jsonb = JsonbBuilder.create();
    String personJson = jsonb.toJson(person);
      
    MatcherAssert.assertThat(personJson, Matchers.is(expectedJson));
  }  
}
