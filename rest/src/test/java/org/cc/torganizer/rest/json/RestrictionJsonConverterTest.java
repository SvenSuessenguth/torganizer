package org.cc.torganizer.rest.json;

import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.GENDER_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;

import java.io.StringReader;
import java.time.LocalDate;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Restriction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestrictionJsonConverterTest {

  private RestrictionJsonConverter converter;

  @BeforeEach
  void setUp() {
    converter = new RestrictionJsonConverter();
  }

  @Test
  void testToModel_AgeRestriction() {
    String jsonString = "{\"id\":1, "
      + "\"discriminator\":\"" + AGE_RESTRICTION.getId() + "\","
      + "\"minDateOfBirth\":\"1968-01-12\","
      + "\"maxDateOfBirth\":\"1970-01-12\"}";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Restriction restriction = converter.toModel(jsonObject, new AgeRestriction());
    assertThat(restriction).isInstanceOf(AgeRestriction.class);

    AgeRestriction ageRestriction = (AgeRestriction) restriction;
    assertThat(ageRestriction.getMinDateOfBirth()).isEqualTo(LocalDate.of(1968, JANUARY, 12));
    assertThat(ageRestriction.getMaxDateOfBirth()).isEqualTo(LocalDate.of(1970, JANUARY, 12));
  }

  @Test
  void testToJson_AgeRestriction() {
    String expectedJsonString = "{\"id\":1,"
      + "\"discriminator\":\"" + AGE_RESTRICTION.getId() + "\","
      + "\"minDateOfBirth\":\"1968-01-12\","
      + "\"maxDateOfBirth\":\"1970-01-12\"}";

    AgeRestriction restriction = new AgeRestriction();
    restriction.setId(1L);
    restriction.setMinDateOfBirth(LocalDate.of(1968, JANUARY, 12));
    restriction.setMaxDateOfBirth(LocalDate.of(1970, JANUARY, 12));

    JsonObject json = converter.toJsonObject(restriction);

    assertThat(json).asString().isEqualTo(expectedJsonString);
  }

  @Test
  void testToModel_GenderRestriction() {
    String jsonString = "{\"id\":1, "
      + "\"discriminator\":\"" + GENDER_RESTRICTION.getId() + "\","
      + "\"gender\":\"MALE\"}";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Restriction restriction = converter.toModel(jsonObject, new GenderRestriction());
    assertThat(restriction).isInstanceOf(GenderRestriction.class);

    GenderRestriction genderRestriction = (GenderRestriction) restriction;
    assertThat(genderRestriction.getGender()).isEqualTo(MALE);

  }

  @Test
  void testToJson_GenderRestriction() {
    String expectedJsonString = "{\"id\":1,"
      + "\"discriminator\":\"" + GENDER_RESTRICTION.getId() + "\","
      + "\"gender\":\"MALE\"}";

    GenderRestriction restriction = new GenderRestriction();
    restriction.setId(1L);
    restriction.setGender(MALE);

    JsonObject json = converter.toJsonObject(restriction);

    assertThat(json).asString().isEqualTo(expectedJsonString);
  }

  @Test
  void testToModel_OpponentTypeRestriction() {
    String jsonString = "{\"id\":1,"
      + "\"discriminator\":\"" + OPPONENT_TYPE_RESTRICTION.getId() + "\","
      + "\"opponentType\":\"PLAYER\"}";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Restriction restriction = converter.toModel(jsonObject, new OpponentTypeRestriction());
    assertThat(restriction).isInstanceOf(OpponentTypeRestriction.class);

    OpponentTypeRestriction opponentTypeRestriction = (OpponentTypeRestriction) restriction;
    assertThat(opponentTypeRestriction.getOpponentType()).isEqualTo(PLAYER);
  }

  @Test
  void testToJson_OpponentTypeRestriction() {
    String expectedJsonString = "{\"id\":1,"
      + "\"discriminator\":\"" + OPPONENT_TYPE_RESTRICTION.getId() + "\","
      + "\"opponentType\":\"PLAYER\"}";

    OpponentTypeRestriction restriction = new OpponentTypeRestriction();
    restriction.setId(1L);
    restriction.setOpponentType(PLAYER);

    JsonObject json = converter.toJsonObject(restriction);

    assertThat(json).asString().isEqualTo(expectedJsonString);
  }
}
