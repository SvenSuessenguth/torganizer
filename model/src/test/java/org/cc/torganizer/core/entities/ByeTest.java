package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class ByeTest {

  @Test
  void testCreateByes_keineLiefertLeereListe() {
    List<Opponent> byes = Bye.createByes(0);

    assertThat(byes).isNotNull();
  }
  
  @Test
  void testCreateByes_negativerWertLiefertLeereListe() {
    List<Opponent> byes = Bye.createByes(-1);
    assertThat(byes).isNotNull();
  }
}
