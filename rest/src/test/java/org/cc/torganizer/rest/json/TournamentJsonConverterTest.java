package org.cc.torganizer.rest.json;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cc.torganizer.core.entities.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * @author svens
 */
class TournamentJsonConverterTest {

  private TournamentJsonConverter converter;

  @BeforeEach
  void before() {
    converter = new TournamentJsonConverter();
  }

  @Test
  void testToJson_withNullValues() {
    String expected = "{\"id\":null,\"name\":\"Turniername\"}";
    Tournament tournament = new Tournament();
    tournament.setName("Turniername");

    final JsonObject jsonObject = converter.toJsonObject(tournament);

    assertThat(jsonObject).asString().isEqualTo(expected);
  }

  @Test
  void testToJsonArray() {
    String expected = "["
        + "{\"id\":1,\"name\":\"Turniername1\"},"
        + "{\"id\":2,\"name\":\"Turniername2\"}"
        + "]";
    Tournament tournament1 = new Tournament();
    tournament1.setName("Turniername1");
    tournament1.setId(1L);
    Tournament tournament2 = new Tournament();
    tournament2.setName("Turniername2");
    tournament2.setId(2L);

    List<Tournament> tournaments = Arrays.asList(tournament1, tournament2);

    final JsonArray jsonArray = converter.toJsonArray(tournaments);

    assertThat(jsonArray).asString().isEqualTo(expected);
  }

  @Test
  void testToList() {
    String jsonString = "["
        + "{\"id\":1,\"name\":\"Turniername1\"},"
        + "{\"id\":2,\"name\":\"Turniername2\"}"
        + "]";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonArray jsonArray = jsonReader.readArray();

    Collection<Tournament> tournaments = Arrays.asList(new Tournament(1L), new Tournament(2L));

    tournaments = converter.toModels(jsonArray, tournaments);

    assertThat(tournaments).hasSize(2);

    Tournament t1 = ((List<Tournament>) tournaments).get(0);
    assertThat(t1.getId()).isEqualTo(1L);
    assertThat(t1.getName()).isEqualTo("Turniername1");
  }
}
