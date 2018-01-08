package org.cc.torganizer.rest.json;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.JsonObject;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author svens
 */
@RunWith(MockitoJUnitRunner.class)
public class SquadJsonConverterTest {
  
  @Spy
  private PlayerJsonConverter playerConverter;
  
  @InjectMocks
  private SquadJsonConverter converter;
  
  @Test
  public void testToJson_validSquad(){
    Player player_0 = new Player("vorname_0", "nachname_0");
    Player player_1 = new Player("vorname_1", "nachname_1");
    Set<Player> players = Stream.of(player_0, player_1).collect(Collectors.toSet());
   
    Squad squad = new Squad();
    squad.setPlayers(players);
    
    final JsonObject jsonObject = converter.toJsonObject(squad);
    
    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    System.out.println(jsonObject.toString());
    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
  }
}
