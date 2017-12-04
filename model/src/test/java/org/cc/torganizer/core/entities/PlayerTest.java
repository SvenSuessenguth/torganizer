package org.cc.torganizer.core.entities;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * @author u000349
 */
public class PlayerTest {

  @Test
  public void testJson() {
    String expectedJson = "{\"bye\":false,\"status\":\"ACTIVE\",\"lastMatch\":null,"
            + "\"person\":{\"dateOfBirth\":null,\"firstName\":\"firstName\",\"gender\":\"UNKNOWN\",\"lastName\":\"lastName\"}}";
    Player player = new Player(new Person("firstName", "lastName"));
    Jsonb jsonb = JsonbBuilder.create();
    String playerJson = jsonb.toJson(player);
    MatcherAssert.assertThat(playerJson, Matchers.is(expectedJson));
  }
}
