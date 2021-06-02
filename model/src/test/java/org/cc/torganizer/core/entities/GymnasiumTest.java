package org.cc.torganizer.core.entities;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GymnasiumTest {

  private Gymnasium gymnasium;

  @BeforeEach
  public void beforeEach() {
    gymnasium = new Gymnasium();
  }

  @Test
  void createCourts_0() {
    gymnasium.createCourts(0);
    List<Court> courts = gymnasium.getCourts();
    Assertions.assertThat(courts).isEmpty();
  }

  @Test
  void createCourts_10() {
    gymnasium.createCourts(10);
    List<Court> courts = gymnasium.getCourts();
    Assertions.assertThat(courts).hasSize(10);
  }

  @Test
  void createCourts_negative() {
    gymnasium.createCourts(-1);
    List<Court> courts = gymnasium.getCourts();
    Assertions.assertThat(courts).hasSize(0);
  }

  @Test
  void createCourts_change() {
    gymnasium.createCourts(10);
    gymnasium.createCourts(5);
    List<Court> courts = gymnasium.getCourts();
    Assertions.assertThat(courts).hasSize(5);
  }
}