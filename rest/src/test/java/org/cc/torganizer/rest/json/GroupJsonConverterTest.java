package org.cc.torganizer.rest.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class GroupJsonConverterTest {


  @Mock
  private PlayerJsonConverter playerConverter;

  @Mock
  private OpponentJsonConverterProvider provider;

  @InjectMocks
  private GroupJsonConverter converter;

  @Test
  void toJsonObject_withId() {
    String expected = "{\"id\":1,\"position\":2}";
    Group group = new Group();
    group.setId(1L);
    group.setPosition(2);

    String actual = converter.toJsonObject(group).toString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void toJsonObject_withNullId() {
    String expected = "{\"id\":null,\"position\":2}";
    Group group = new Group();
    group.setPosition(2);

    String actual = converter.toJsonObject(group).toString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void toJsonArray() {
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

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void toModel() {
    String jsonString = "{\"id\":1,\"position\":2}";
    Group group = new Group();
    group.setId(1L);

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    group = converter.toModel(jsonObject, group);

    assertThat(group.getId()).isEqualTo(1L);
    assertThat(group.getPosition()).isEqualTo(2);
  }

  @Test
  void toModel_withIdNull() {
    String jsonString = "{\"id\":null,\"position\":2}";
    Group group = new Group();

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    group = converter.toModel(jsonObject, group);

    assertThat(group.getId()).isNull();
    assertThat(group.getPosition()).isEqualTo(2);
  }

  @Test
  void toModels() {
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

    converter.toModels(jsonArray, groups);

    assertThat(group1.getPosition()).isEqualTo(1);
    assertThat(group2.getPosition()).isEqualTo(3);
  }

  @Test
  void addOpponents(){
    String jsonGroupString = "{\"id\":1,\"position\":1}";
    JsonReader jsonGroupReader = Json.createReader(new StringReader(jsonGroupString));
    JsonObject jsonGroup = jsonGroupReader.readObject();

    String jsonPlayerString = "[{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
        + "\"person\":{"
        + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
        + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
        + "\"club\":{\"id\":null,\"name\":null}}]";
    JsonReader jsonPlayerReader = Json.createReader(new StringReader(jsonPlayerString));
    JsonArray jsonPlayers = jsonPlayerReader.readArray();

    when(provider.getConverter(any(Collection.class))).thenReturn(playerConverter);
    when(playerConverter.toJsonArray(any(Collection.class))).thenReturn(jsonPlayers);

    // Test
    JsonObject patchedJsonGroup = converter.addOpponents(jsonGroup, new ArrayList<>());

    JsonObject opponent = (JsonObject) patchedJsonGroup.getJsonArray("opponents").get(0);
    String vornameFromPatchesJsonGroup = opponent.getJsonObject("person").getJsonString("firstName").getString();

    assertThat(vornameFromPatchesJsonGroup).isEqualTo("vorname");
  }
}