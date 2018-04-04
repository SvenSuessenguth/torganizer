package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.time.LocalDateTime.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author svens
 */
@RunWith(MockitoJUnitRunner.class)
public class PlayerJsonConverterTest {

  @Spy
  private PersonJsonConverter personConverter;

  @InjectMocks
  private PlayerJsonConverter converter;

  @Test
  public void testToJson_withNullValues() {
    String expected = "{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
      + "\"person\":{"
      + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
      + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}";
    Player player = new Player("vorname", "nachname");

    final JsonObject jsonObject = converter.toJsonObject(player);

    assertThat(jsonObject.toString(), is(expected));
  }

  @Test
  public void testToJson_withLastMatch() {
    String expected = "{\"id\":null,\"lastMatch\":\"2017-12-24 18:00:00\",\"status\":\"ACTIVE\","
      + "\"person\":{"
      + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
      + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}";
    Player player = new Player("vorname", "nachname");
    LocalDateTime christmasEve = of(2017, Month.DECEMBER, 24, 18, 0, 0);
    player.setLastMatch(christmasEve);

    final JsonObject jsonObject = converter.toJsonObject(player);

    assertThat(jsonObject.toString(), is(expected));
  }

  @Test
  public void testToModel_singlePlayer() {
    String jsonString = "{\"id\":null,\"lastMatch\":\"2017-12-24 18:00:00\","
      + "\"person\":{"
      + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
      + "\"dateOfBirth\":null,\"gender\":\"MALE\"}}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();

    Player player = converter.toModel(jsonObject, new Player(new Person()));

    LocalDateTime expectedLastMatch = LocalDateTime.of(2017, Month.DECEMBER, 24, 18, 0, 0);
    assertThat(player.getLastMatch(), is(expectedLastMatch));
    assertThat(player.getPerson().getFirstName(), is("vorname"));
    assertThat(player.getPerson().getGender(), is(Gender.MALE));
  }

  @Test
  public void testToModels_multiplePlayer() {
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


    players = (List<Player>) converter.toModels(jsonArray, players);

    assertThat(players, is(notNullValue()));
    assertThat(players.size(), is(2));


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
    LocalDateTime expectedLastMatch = LocalDateTime.of(2017, Month.DECEMBER, 24, 18, 0, 0);
    assertThat(player1.getLastMatch(), is(expectedLastMatch));
    assertThat(player1.getPerson().getFirstName(), is("vorname"));
    assertThat(player1.getPerson().getGender(), is(Gender.MALE));

    LocalDate expectedDateOfBirth = LocalDate.of(1968, Month.JANUARY, 12);
    assertThat(player2.getPerson().getDateOfBirth(), is(expectedDateOfBirth));
  }
}
