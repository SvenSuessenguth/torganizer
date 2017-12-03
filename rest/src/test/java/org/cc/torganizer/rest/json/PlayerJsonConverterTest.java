package org.cc.torganizer.rest.json;

import java.time.LocalDateTime;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Status;
import org.junit.Before;
import org.junit.Test;

public class PlayerJsonConverterTest {

  private PlayerJsonConverter converter;

  @Before
  public void before() {
    converter = new PlayerJsonConverter();
  }

  @Test
  public void testAdaptToJson_allFieldsAreSet() throws Exception {
    Player player = new Player(new Person());
    player.setId(1L);
    player.setStatus(Status.ACTIVE);
    player.setLastMatch(LocalDateTime.of(2017, 11, 27, 15, 30));

    String json = converter.adaptToJson(player).toString();

    System.out.println(json);
  }

}
