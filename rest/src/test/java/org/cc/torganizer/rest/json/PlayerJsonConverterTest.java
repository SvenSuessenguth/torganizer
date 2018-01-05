package org.cc.torganizer.rest.json;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.of;
import java.time.Month;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Player;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

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
  public void testToJson_withNullValues(){
    String expected ="{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
            + "\"person\":{"
            + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}";
    Player player = new Player("vorname", "nachname");
    
    final JsonObject jsonObject = converter.toJsonObject(player);
    
    assertThat(jsonObject.toString(), is(expected));
  }
  
  @Test
  public void testToJson_withLastMatch(){
    String expected ="{\"id\":null,\"lastMatch\":\"2017-12-24 18:00:00\",\"status\":\"ACTIVE\","
            + "\"person\":{"
            + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}";
    Player player = new Player("vorname", "nachname");
    LocalDateTime christmasEve = of(2017, Month.DECEMBER, 24, 18, 0,0);
    player.setLastMatch(christmasEve);
    
    final JsonObject jsonObject = converter.toJsonObject(player);
    
    assertThat(jsonObject.toString(), is(expected));
  }
  
  @Test
  public void testToModel_singlePlayer(){
    String jsonString ="{\"id\":null,\"lastMatch\":\"2017-12-24 18:00:00\","
            + "\"person\":{"
            + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":null,\"gender\":\"MALE\"}}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();     
    
    Player player = converter.toModel(jsonObject);
    
    LocalDateTime expectedLastMatch = LocalDateTime.of(2017, Month.DECEMBER, 24, 18, 0, 0);
    assertThat(player.getLastMatch(), is(expectedLastMatch));
    assertThat(player.getPerson().getFirstName(), is("vorname"));
    assertThat(player.getPerson().getGender(), is(Gender.MALE));
  }
  
  @Test
  public void testToModels_multiplePlayer(){
    String jsonString ="["
            + "{\"id\":null,\"lastMatch\":\"2017-12-24 18:00:00\","
            + "\"person\":{"
            + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":null,\"gender\":\"MALE\"}},"
            
            +"{\"id\":null,\"lastMatch\":null,"
            + "\"person\":{"
            + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":\"1968-01-12\",\"gender\":\"MALE\"}}"
            + "]";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonArray jsonArray = jsonReader.readArray();
    
    List<Player> players = (List<Player>)converter.toModels(jsonArray);
    
    assertThat(players, is(notNullValue()));
    assertThat(players.size(), is(2));
    
    Player player0 = players.get(0);
    LocalDateTime expectedLastMatch = LocalDateTime.of(2017, Month.DECEMBER, 24, 18, 0, 0);
    assertThat(player0.getLastMatch(), is(expectedLastMatch));
    assertThat(player0.getPerson().getFirstName(), is("vorname"));
    assertThat(player0.getPerson().getGender(), is(Gender.MALE));
    
    Player player1 = players.get(1);
    LocalDate expectedDateOfBirth = LocalDate.of(1968, Month.JANUARY, 12);
    assertThat(player1.getPerson().getDateOfBirth(), is(expectedDateOfBirth));
  }
}
