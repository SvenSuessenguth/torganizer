package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
