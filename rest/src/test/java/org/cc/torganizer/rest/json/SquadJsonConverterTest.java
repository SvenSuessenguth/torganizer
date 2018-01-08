package org.cc.torganizer.rest.json;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
            + "]},"
            + "{\"id\":null,"
            + "\"players\":["
            + "{\"id\":null,\"lastMatch\":null,\"status\":\"ACTIVE\","
            + "\"person\":{\"id\":null,\"firstName\":\"vorname_2\",\"lastName\":\"nachname_2\",\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}"
            + "]}"
            + "]";
    
    Player player_0 = new Player("vorname_0", "nachname_0");
    Set<Player> players_0 = Stream.of(player_0).collect(Collectors.toSet());   
    Squad squad_0 = new Squad();
    squad_0.setPlayers(players_0);
    
    Player player_2 = new Player("vorname_2", "nachname_2");
    Set<Player> players_1 = Stream.of(player_2).collect(Collectors.toSet());   
    Squad squad_1 = new Squad();
    squad_1.setPlayers(players_1);
    
    Set<Squad> squads = Stream.of(squad_0, squad_1).collect(Collectors.toSet());   
    
    final JsonArray jsonArray = converter.toJsonArray(squads);
    
    System.out.println(jsonArray.toString());
    
    assertThat(jsonArray.toString(), is(expected));
  }
}
