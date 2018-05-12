package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author svens
 */
public class SquadJsonConverterTest {
  
  private SquadJsonConverter converter;
  
  @BeforeEach
  public void before(){
    PersonJsonConverter personConverter = new PersonJsonConverter();
    PlayerJsonConverter playerConverter = new PlayerJsonConverter(personConverter, new ClubJsonConverter());
    
    converter = new SquadJsonConverter(playerConverter);
  }
  
  @Test
  public void testToJsonObject(){
    String expected = "{\"id\":null,"
            + "\"players\":["
            + "{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
            + "\"person\":{\"id\":null,\"firstName\":\"vorname_0\",\"lastName\":\"nachname_0\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
            + "\"club\":{\"id\":null,\"name\":null}}"
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
            + "\"person\":{\"id\":null,\"firstName\":\"vorname_0\",\"lastName\":\"nachname_0\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
            + "\"club\":{\"id\":null,\"name\":null}}"
            + "]}"
            + "]";
    
    Player player_0 = new Player("vorname_0", "nachname_0");
    Set<Player> players_0 = Stream.of(player_0).collect(Collectors.toSet());   
    Squad squad_0 = new Squad();
    squad_0.setPlayers(players_0);
    
    Set<Squad> squads = Stream.of(squad_0).collect(Collectors.toSet());   
    
    final JsonArray jsonArray = converter.toJsonArray(squads);

    assertThat(jsonArray.toString(), is(expected));
  }
  
  @Test
  public void testToModel(){
    String jsonString = "{\"id\":1,"
            + "\"players\":["
            + "{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
            + "\"person\":{\"id\":null,\"firstName\":\"vorname_0\",\"lastName\":\"nachname_0\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"},"
            + "\"club\":{\"id\":null,\"name\":null}}"
            + "]}";

    JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
    JsonObject jsonObject = jsonReader.readObject();
    
    final Squad squad = converter.toModel(jsonObject, new Squad(1L));
    System.out.println(squad);
    assertThat(squad, is(notNullValue()));
    
  }
}
