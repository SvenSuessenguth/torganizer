package org.cc.torganizer.rest.json;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.json.JsonObject;

import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Player;
import org.junit.Before;
import org.junit.Test;

public class PlayerJsonConverterTest {

  private PlayerJsonConverter converter;

  @Before
  public void before() {
    converter = new PlayerJsonConverter();
  }
  
  @Test
  public void testToJsonPlayer() throws Exception {
    Player p = new Player("firstName", "lastName");
    p.getPerson().setGender(Gender.MALE);

    JsonObject json = converter.toJson(p);

    Player p2 = converter.toObject(json);
    assertThat(p2.getPerson().getGender(), is(Gender.MALE));
  }
}
