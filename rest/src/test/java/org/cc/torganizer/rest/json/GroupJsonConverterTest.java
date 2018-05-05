package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Group;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class GroupJsonConverterTest {

  private GroupJsonConverter converter;

  @Before
  public void setUp() throws Exception {
    this.converter = new GroupJsonConverter();
  }

  @Test
  public void toJsonObject_withId() {
    String expected = "{\"id\":1,\"position\":2}";
    Group group = new Group();
    group.setId(1L);
    group.setPosition(2);

    String actual = converter.toJsonObject(group).toString();

    assertThat(actual, is(expected));
  }

  @Test
  public void toJsonObject_withNullId() {
    String expected = "{\"id\":null,\"position\":2}";
    Group group = new Group();
    group.setPosition(2);

    String actual = converter.toJsonObject(group).toString();

    assertThat(actual, is(expected));
  }

  @Test
  public void toJsonArray() {
    String expected = "[" +
      "{\"id\":null,\"position\":1}," +
      "{\"id\":2,\"position\":3}" +
      "]";

    Group group1 = new Group();
    group1.setPosition(1);
    Group group2 = new Group();
    group2.setId(2L);
    group2.setPosition(3);

    Collection<Group> groups = new ArrayList<>(2);
    groups.add(group1);
    groups.add(group2);

    String actual = converter.toJsonArray(groups).toString();

    assertThat(actual, is(expected));
  }

  @Test
  public void toModel() {
    String jsonString = "{\"id\":1,\"position\":2}";
    Group group = new Group();
    group.setId(1L);

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    group = converter.toModel(jsonObject, group);

    assertThat(group.getId(), is(1L));
    assertThat(group.getPosition(), is(2));
  }

  @Test
  public void toModel_withIdNull() {
    String jsonString = "{\"id\":null,\"position\":2}";
    Group group = new Group();

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    group = converter.toModel(jsonObject, group);

    assertThat(group.getId(), is(nullValue()));
    assertThat(group.getPosition(), is(2));
  }

  @Test
  public void toModels() {
    String jsonString = "[" +
      "{\"id\":1,\"position\":1}," +
      "{\"id\":2,\"position\":3}" +
      "]";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonArray jsonArray = jsonReader.readArray();

    Group group1 = new Group();
    group1.setId(1L);
    Group group2 = new Group();
    group2.setId(2L);
    Collection<Group> groups = new ArrayList<>(2);
    groups.add(group1);
    groups.add(group2);

    groups = converter.toModels(jsonArray, groups);

    assertThat(group1.getPosition(), is(1));
    assertThat(group2.getPosition(), is(3));
  }
}