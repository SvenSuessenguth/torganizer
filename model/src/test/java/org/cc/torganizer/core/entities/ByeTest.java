package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ByeTest {

  @Test
  public void testCreateByes_keineLiefertLeereListe() throws Exception {
    List<Opponent> byes = Bye.createByes(0);
    assertThat(byes, is(notNullValue()));
  }
  
  @Test
  public void testCreateByes_negativerWertLiefertLeereListe() throws Exception {
    List<Opponent> byes = Bye.createByes(-1);
    assertThat(byes, is(notNullValue()));
  }
}
