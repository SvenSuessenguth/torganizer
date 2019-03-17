package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ByeTest {

  @Test
  public void testCreateByes_keineLiefertLeereListe() throws Exception {
    List<Opponent> byes = Bye.createByes(0);

    assertThat(byes).isNotNull();
  }
  
  @Test
  public void testCreateByes_negativerWertLiefertLeereListe() throws Exception {
    List<Opponent> byes = Bye.createByes(-1);
    assertThat(byes).isNotNull();
  }
}
