package org.cc.torganizer.core.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.junit.Test;

public class TournamentTest {

  @Test
  public void testTournamentJsonb() throws Exception {
    Tournament tournament = new Tournament();
    tournament.setName("Meisterschaft");
    

    Jsonb jsonb = JsonbBuilder.create();
    String tournamentJson = jsonb.toJson(tournament);
    
    String expectedJson = "{\"name\":\"Meisterschaft\"}";
    assertThat(tournamentJson, is(expectedJson));
  }

}
