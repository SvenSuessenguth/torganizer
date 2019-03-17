package org.cc.torganizer.rest.json;

import static java.time.LocalDateTime.of;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.MALE;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author svens
 */
@ExtendWith(MockitoExtension.class)
class PlayerJsonConverterTest {

  @Spy
  private PersonJsonConverter personConverter;

  @Spy
  private ClubJsonConverter clubJsonConverter;

  @InjectMocks
  private PlayerJsonConverter converter;

  @Test
  void testToJson_withNullValues() {
    String expected = "{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
      + "\"person\":{"
      + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
      + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
      + "\"club\":{\"id\":null,\"name\":null}}";
    Player player = new Player("vorname", "nachname");

    final JsonObject jsonObject = converter.toJsonObject(player);

    assertThat(jsonObject).asString().isEqualTo(expected);
  }

  @Test
  void testToJson_withLastMatch() {
    String expected = "{\"id\":null,\"lastMatch\":\"2017-12-24 18:00:00\",\"status\":\"ACTIVE\","
      + "\"person\":{"
      + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
      + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
      + "\"club\":{\"id\":null,\"name\":null}}";
    Player player = new Player("vorname", "nachname");
    LocalDateTime christmasEve = of(2017, DECEMBER, 24, 18, 0, 0);
    player.setLastMatch(christmasEve);

    final JsonObject jsonObject = converter.toJsonObject(player);

    assertThat(jsonObject).asString().isEqualTo(expected);
  }

  @Test
  void testToModel_singlePlayer() {
    String jsonString = "{\"id\":null,\"lastMatch\":\"2017-12-24 18:00:00\","
      + "\"person\":{"
      + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
      + "\"dateOfBirth\":null,\"gender\":\"MALE\"}}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Player player = converter.toModel(jsonObject, new Player(new Person()));

    LocalDateTime expectedLastMatch = LocalDateTime.of(2017, DECEMBER, 24, 18, 0, 0);
    assertThat(player.getLastMatch()).isEqualTo(expectedLastMatch);
    assertThat(player.getPerson().getFirstName()).isEqualTo("vorname");
    assertThat(player.getPerson().getGender()).isEqualTo(MALE);
  }

  @Test
  void testToModels_multiplePlayer() throws NullPointerException{
    String jsonString = "["
      + "{\"id\":1,\"lastMatch\":\"2017-12-24 18:00:00\","
      + "\"person\":{"
      + "\"id\":1,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
      + "\"dateOfBirth\":null,\"gender\":\"MALE\"}},"
      + "{\"id\":2,\"lastMatch\":null,"
      + "\"person\":{"
      + "\"id\":2,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
      + "\"dateOfBirth\":\"1968-01-12\",\"gender\":\"MALE\"}}"
      + "]";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonArray jsonArray = jsonReader.readArray();
    Player p1 = new Player(1L);
    p1.setPerson(new Person(1L));
    Player p2 = new Player(2L);
    p2.setPerson(new Person(2L));
    Collection<Player> players = Arrays.asList(p1, p2);


    players = converter.toModels(jsonArray, players);

    assertThat(players).isNotNull();
    assertThat(players).hasSize(2);


    Player player1 = null;
    Player player2 = null;
    for(Player p : players){
      if(Objects.equals(p.getId(), 1L)){
        player1 = p;
      }
      if(Objects.equals(p.getId(), 2L)){
        player2 = p;
      }
    }
    LocalDateTime expectedLastMatch = LocalDateTime.of(2017, DECEMBER, 24, 18, 0, 0);
    assertThat(Objects.requireNonNull(player1).getLastMatch()).isEqualTo(expectedLastMatch);
    assertThat(player1.getPerson().getFirstName()).isEqualTo("vorname");
    assertThat(player1.getPerson().getGender()).isEqualTo(MALE);

    LocalDate expectedDateOfBirth = LocalDate.of(1968, JANUARY, 12);
    assertThat(Objects.requireNonNull(player2).getPerson().getDateOfBirth()).isEqualTo(expectedDateOfBirth);
  }
}
