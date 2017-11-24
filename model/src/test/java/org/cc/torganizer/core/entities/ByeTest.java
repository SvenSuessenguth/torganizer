package org.cc.torganizer.core.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.List;

import org.junit.Test;

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
