package org.cc.torganizer.rest.json;

import java.io.StringReader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author svens
 */
public class SquadJsonConverterTest {
  
  private SquadJsonConverter converter;
  
  @Before
  public void before(){
    PersonJsonConverter personConverter = new PersonJsonConverter();
    PlayerJsonConverter playerConverter = new PlayerJsonConverter(personConverter);
    
    converter = new SquadJsonConverter(playerConverter);
  }
  
  @Test
  public void testToJsonObject(){
    String expected = "{\"id\":null,"
            + "\"players\":["
            + "{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
            + "\"person\":{\"id\":null,\"firstName\":\"vorname_0\",\"lastName\":\"nachname_0\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}"
            + "]}";
    
    Player player_0 = new Player("vorname_0", "nachname_0");
    Set<Player> players = Stream.of(player_0).collect(Collectors.toSet());
   
    Squad squad = new Squad();
    squad.setPlayers(players);
    
    final JsonObject jsonObject = converter.toJsonObject(squad);
    
    assertThat(jsonObject.toString(), is(expected));
  }
  
  @Test
  public void testToJsonArray(){
    
    String expected = "["
            + "{\"id\":null,"
            + "\"players\":["
            + "{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
            + "\"person\":{\"id\":null,\"firstName\":\"vorname_0\",\"lastName\":\"nachname_0\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}"
            + "]}"
            + "]";
    
    Player player_0 = new Player("vorname_0", "nachname_0");
    Set<Player> players_0 = Stream.of(player_0).collect(Collectors.toSet());   
    Squad squad_0 = new Squad();
    squad_0.setPlayers(players_0);
    
    Set<Squad> squads = Stream.of(squad_0).collect(Collectors.toSet());   
    
    final JsonArray jsonArray = converter.toJsonArray(squads);
    
    System.out.println(jsonArray.toString());
    
    assertThat(jsonArray.toString(), is(expected));
  }
  
  @Test
  public void testToModel(){
    String jsonString = "{\"id\":null,"
            + "\"players\":["
            + "{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
            + "\"person\":{\"id\":null,\"firstName\":\"vorname_0\",\"lastName\":\"nachname_0\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}"
            + "]}";
    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();
    
    final Squad squad = converter.toModel(jsonObject);
    System.out.println(squad);
    assertThat(squad, is(notNullValue()));
    
  }
}
