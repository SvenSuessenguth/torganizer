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
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
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
  public void testToJson_AgeRestriction() {
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

  @Test
  public void testToModel_GenderRestriction() {
    String jsonString = "{\"id\":1, "
      + "\"discriminator\":\"" + Restriction.Discriminator.GENDER_RESTRICTION.getId() + "\","
      + "\"validGender\":\"MALE\"}";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Restriction restriction = converter.toModel(jsonObject);
    MatcherAssert.assertThat(restriction, Matchers.is(Matchers.instanceOf(GenderRestriction.class)));

    GenderRestriction genderRestriction = (GenderRestriction) restriction;
    MatcherAssert.assertThat(genderRestriction.getValidGender(), Matchers.is(Gender.MALE));

  }

  @Test
  public void testToJson_GenderRestriction() {
    String expectedJsonString = "{\"id\":1,"
      + "\"discriminator\":\"" + Restriction.Discriminator.GENDER_RESTRICTION.getId() + "\","
      + "\"validGender\":\"MALE\"}";

    GenderRestriction restriction = new GenderRestriction();
    restriction.setId(1L);
    restriction.setValidGender(Gender.MALE);

    JsonObject jsonObject = converter.toJsonObject(restriction);
    String actualJsonString = jsonObject.toString();

    MatcherAssert.assertThat(expectedJsonString, Matchers.is(actualJsonString));
  }

  @Test
  public void testToModel_OpponentTypeRestriction() {
    String jsonString = "{\"id\":1,"
      + "\"discriminator\":\"" + Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION.getId() + "\","
      + "\"validOpponentType\":\"PLAYER\"}";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Restriction restriction = converter.toModel(jsonObject);
    MatcherAssert.assertThat(restriction, Matchers.is(Matchers.instanceOf(OpponentTypeRestriction.class)));

    OpponentTypeRestriction opponentTypeRestriction = (OpponentTypeRestriction) restriction;
    MatcherAssert.assertThat(opponentTypeRestriction.getValidOpponentType(), Matchers.is(OpponentType.PLAYER));

  }

  @Test
  public void testToJson_OpponentTypeRestriction() {
    String expectedJsonString = "{\"id\":1,"
      + "\"discriminator\":\"" + Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION.getId() + "\","
      + "\"validOpponentType\":\"PLAYER\"}";

    OpponentTypeRestriction restriction = new OpponentTypeRestriction();
    restriction.setId(1L);
    restriction.setValidOpponentType(OpponentType.PLAYER);

    JsonObject jsonObject = converter.toJsonObject(restriction);
    String actualJsonString = jsonObject.toString();

    MatcherAssert.assertThat(expectedJsonString, Matchers.is(actualJsonString));
  }
}
