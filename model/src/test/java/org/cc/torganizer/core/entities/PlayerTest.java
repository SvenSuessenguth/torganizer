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
  public void testCreateJson() {
    String expectedJson = "{\"bye\":false,\"status\":\"ACTIVE\",\"lastMatch\":null,"
            + "\"person\":{\"dateOfBirth\":null,\"firstName\":\"firstName\",\"gender\":\"UNKNOWN\",\"lastName\":\"lastName\"}}";
    Player player = new Player(new Person("firstName", "lastName"));
    Jsonb jsonb = JsonbBuilder.create();
    String playerJson = jsonb.toJson(player);
    MatcherAssert.assertThat(playerJson, Matchers.is(expectedJson));
  }
  
  public void testFromJson(){
    // das ist ein Json, der vom Client an den Rest-Service geschickt wird
    String playerJson = "player: {\"id\":0,\"status\":\"ACTIVE\",\"person\":{\"id\":0,\"firstName\":\"a\",\"lastName\":\"a\",\"dateOfBirthISO\":\"2017-12-04\",\"gender\":\"MALE\"}}";
    Jsonb jsonb = JsonbBuilder.create();
    Player player = jsonb.fromJson(playerJson, Player.class);
    
    MatcherAssert.assertThat(player, Matchers.is(Matchers.notNullValue()));
    MatcherAssert.assertThat(player.getStatus(), Matchers.is(Status.ACTIVE));
    MatcherAssert.assertThat(player.getPerson(), Matchers.is(Matchers.notNullValue()));
    MatcherAssert.assertThat(player.getPerson().getFirstName(), Matchers.is("a"));
  }
}
