/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.Month;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Restriction;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class RestrictionJsonConverterTest {

  private RestrictionJsonConverter converter;

  @Before
  public void setUp() {
    converter = new RestrictionJsonConverter();
  }

  @Test
  public void testToModel_AgeRestriction() {
    String jsonString = "{\"id\":1, "
      + "\"discriminator\":\"" + Restriction.Discriminator.AGE_RESTRICTION.getId() + "\","
      + "\"minDateOfBirth\":\"1968-01-12\","
      + "\"maxDateOfBirth\":\"1970-01-12\"}";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Restriction restriction = converter.toModel(jsonObject);
    MatcherAssert.assertThat(restriction, Matchers.is(Matchers.instanceOf(AgeRestriction.class)));

    AgeRestriction ageRestriction = (AgeRestriction) restriction;
    MatcherAssert.assertThat(ageRestriction.getMinDateOfBirth(), Matchers.is(LocalDate.of(1968, Month.JANUARY, 12)));
    MatcherAssert.assertThat(ageRestriction.getMaxDateOfBirth(), Matchers.is(LocalDate.of(1970, Month.JANUARY, 12)));
  }

  @Test
  public void testToJsonl_AgeRestriction() {
    String expectedJsonString = "{\"id\":1,"
      + "\"discriminator\":\"" + Restriction.Discriminator.AGE_RESTRICTION.getId() + "\","
      + "\"minDateOfBirth\":\"1968-01-12\","
      + "\"maxDateOfBirth\":\"1970-01-12\"}";

    AgeRestriction restriction = new AgeRestriction();
    restriction.setId(1L);
    restriction.setMinDateOfBirth(LocalDate.of(1968, Month.JANUARY, 12));
    restriction.setMaxDateOfBirth(LocalDate.of(1970, Month.JANUARY, 12));

    JsonObject jsonObject = converter.toJsonObject(restriction);
    String actualJsonString = jsonObject.toString();

    MatcherAssert.assertThat(expectedJsonString, Matchers.is(actualJsonString));
  }
}
