package org.cc.torganizer.rest.json;

import java.time.LocalDateTime;
import static java.time.LocalDateTime.of;
import java.time.Month;
import javax.json.JsonObject;
import org.cc.torganizer.core.entities.Player;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
    String expected ="{\"id\":null,\"lastMatch\":null,"
            + "\"person\":{"
            + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}";
    Player player = new Player("vorname", "nachname");
    
    final JsonObject jsonObject = converter.toJsonObject(player);
    
    assertThat(jsonObject.toString(), is(expected));
  }
  
  @Test
  public void testToJson_withLastMatch(){
    String expected ="{\"id\":null,\"lastMatch\":\"2017-12-24 18:00:00\","
            + "\"person\":{"
            + "\"id\":null,\"firstName\":\"vorname\",\"lastName\":\"nachname\","
            + "\"dateOfBirth\":null,\"gender\":\"UNKNOWN\"}}";
    Player player = new Player("vorname", "nachname");
    LocalDateTime christmasEve = of(2017, Month.DECEMBER, 24, 18, 0,0);
    player.setLastMatch(christmasEve);
    
    final JsonObject jsonObject = converter.toJsonObject(player);
    
    assertThat(jsonObject.toString(), is(expected));
  }
}
