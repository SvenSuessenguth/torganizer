package org.cc.torganizer.rest.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.System.DOUBLE_ELIMINATION;
import static org.cc.torganizer.core.entities.System.ROUND_ROBIN;
import static org.cc.torganizer.core.entities.System.SINGLE_ELIMINATION;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cc.torganizer.core.entities.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoundJsonConverterTest {

  private RoundJsonConverter converter;

  @BeforeEach
  void before() {
    converter = new RoundJsonConverter();
  }

  @Test
  void testToJsonObject_empty() {
    String expected = "{\"id\":null,\"qualified\":0,\"position\":null,\"system\":\"DOUBLE_ELIMINATION\"}";

    Round round = new Round();
    round.setSystem(DOUBLE_ELIMINATION);
    String actual = converter.toJsonObject(round).toString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testToJsonObject_full() {
    String expected = "{\"id\":1,\"qualified\":4,\"position\":1,\"system\":\"DOUBLE_ELIMINATION\"}";

    Round round = new Round();
    round.setId(1L);
    round.setPosition(1);
    round.setSystem(DOUBLE_ELIMINATION);
    round.setQualified(4);

    String actual = converter.toJsonObject(round).toString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testToJsonArray_empty() {
    String expected = "[]";
    List<Round> rounds = new ArrayList<>();
    String actual = converter.toJsonArray(rounds).toString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testToJsonArray_full() {
    String expected = "[" +
        "{\"id\":1,\"qualified\":4,\"position\":1,\"system\":\"ROUND_ROBIN\"}," +
        "{\"id\":2,\"qualified\":2,\"position\":2,\"system\":null}" +
        "]";

    Collection<Round> rounds = new ArrayList<>(2);

    Round round1 = new Round();
    round1.setId(1L);
    round1.setQualified(4);
    round1.setPosition(1);
    round1.setSystem(ROUND_ROBIN);
    rounds.add(round1);

    Round round2 = new Round();
    round2.setId(2L);
    round2.setQualified(2);
    round2.setPosition(2);
    round2.setSystem(null);
    rounds.add(round2);

    String actual = converter.toJsonArray(rounds).toString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testToModel_withValues() {
    String jsonString = "{\"id\":1,\"qualified\":4,\"position\":1,\"system\":\"SINGLE_ELIMINATION\"}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Round round = new Round(1L);

    converter.toModel(jsonObject, round);

    assertThat(round.getId()).isEqualTo(1L);
    assertThat(round.getQualified()).isEqualTo(4);
    assertThat(round.getPosition()).isEqualTo(1);
    assertThat(round.getSystem()).isEqualTo(SINGLE_ELIMINATION);
  }

  @Test
  void testToModel_withNullValues() {
    String jsonString = "{\"id\":null,\"qualified\":null,\"position\":null,\"system\":null}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Round round = new Round();

    converter.toModel(jsonObject, round);

    assertThat(round.getId()).isNull();
    assertThat(round.getQualified()).isEqualTo(0);
    assertThat(round.getPosition()).isNull();
    assertThat(round.getSystem()).isEqualTo(ROUND_ROBIN);
  }

  @Test
  void testToModels() {
    String jsonString = "["
        + "{\"id\":1,\"qualified\":4,\"position\":1},"
        + "{\"id\":2,\"qualified\":2,\"position\":2}"
        + "]";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonArray jsonArray = jsonReader.readArray();
    Collection<Round> rounds = Arrays.asList(new Round(1L), new Round(2L));

    rounds = converter.toModels(jsonArray, rounds);

    assertThat(rounds).isNotNull();
    assertThat(rounds).hasSize(2);
  }
}