package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class RoundJsonConverterTest {

  private RoundJsonConverter converter;

  @BeforeEach
  public void before(){
    converter = new RoundJsonConverter();
  }

  @Test
  public void testToJsonObject_empty(){
    String expected = "{\"id\":null,\"qualified\":0,\"position\":null,\"system\":\"DOUBLE_ELIMINATION\"}";

    Round round = new Round();
    round.setSystem(System.DOUBLE_ELIMINATION);
    String actual = converter.toJsonObject(round).toString();

    assertThat(actual, is(expected));
  }

  @Test
  public void testToJsonObject_full(){
    String expected = "{\"id\":1,\"qualified\":4,\"position\":1,\"system\":\"DOUBLE_ELIMINATION\"}";

    Round round = new Round();
    round.setId(1L);
    round.setPosition(1);
    round.setSystem(System.DOUBLE_ELIMINATION);
    round.setQualified(4);

    String actual = converter.toJsonObject(round).toString();

    assertThat(actual, is(expected));
  }

  @Test
  public void testToJsonArray_empty(){
    String expected = "[]";
    List<Round> rounds = new ArrayList<>();
    String actual = converter.toJsonArray(rounds).toString();

    assertThat(actual, is(expected));
  }

  @Test
  public void testToJsonArray_full(){
    String expected = "["+
      "{\"id\":1,\"qualified\":4,\"position\":1,\"system\":\"ROUND_ROBIN\"}," +
      "{\"id\":2,\"qualified\":2,\"position\":2,\"system\":null}" +
      "]";

    Collection<Round> rounds = new ArrayList<>(2);

    Round round1 = new Round();
    round1.setId(1L);
    round1.setQualified(4);
    round1.setPosition(1);
    round1.setSystem(System.ROUND_ROBIN);
    rounds.add(round1);

    Round round2 = new Round();
    round2.setId(2L);
    round2.setQualified(2);
    round2.setPosition(2);
    round2.setSystem(null);
    rounds.add(round2);

    String actual = converter.toJsonArray(rounds).toString();

    assertThat(actual, is(expected));
  }

  @Test
  public void testToModel_withValues(){
    String jsonString = "{\"id\":1,\"qualified\":4,\"position\":1,\"system\":\"SINGLE_ELIMINATION\"}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Round round = new Round(1L);

    converter.toModel(jsonObject, round);

    assertThat(1L, is(round.getId()));
    assertThat(4, is(round.getQualified()));
    assertThat(1, is(round.getPosition()));
    assertThat(round.getSystem(), is(System.SINGLE_ELIMINATION));
  }

  @Test
  public void testToModel_withNullValues(){
    String jsonString = "{\"id\":null,\"qualified\":null,\"position\":null,\"system\":null}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Round round = new Round();

    converter.toModel(jsonObject, round);

    assertThat(round.getId(), is(nullValue()));
    assertThat(round.getQualified(), is(0));
    assertThat(round.getPosition(), is(nullValue()));
    assertThat(round.getSystem(), is(System.ROUND_ROBIN));
  }

  @Test
  public void testToModels() {
    String jsonString = "["
      + "{\"id\":1,\"qualified\":4,\"position\":1},"
      + "{\"id\":2,\"qualified\":2,\"position\":2}"
      + "]";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonArray jsonArray = jsonReader.readArray();
    Collection<Round> rounds = Arrays.asList(new Round(1L), new Round(2L));

    rounds = converter.toModels(jsonArray, rounds);

    assertThat(rounds, is(notNullValue()));
    assertThat(rounds.size(), is(2));
  }
}