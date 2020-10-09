package org.cc.torganizer.core.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;

import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerBuilderTest {

  PlayerBuilder builder;

  @BeforeEach
  void beforeEach() {
    builder = new PlayerBuilder();
  }

  @Test
  void testStandard() {
    Player p = builder.get();

    assertThat(p.getPerson().getFirstName()).isEqualTo("firstName");
    assertThat(p.getPerson().getLastName()).isEqualTo("lastName");
    assertThat(p.getPerson().getAge()).isEqualTo(20);
    assertThat(p.getPerson().getGender()).isEqualTo(MALE);
  }

  @Test
  void testWithAge_0() {
    Player p = builder.withAge(0).get();
    assertThat(p.getPerson().getAge()).isEqualTo(0);
  }

  @Test
  void testWithAge_minus1() {
    Player p = builder.withAge(-1).get();
    assertThat(p.getPerson().getAge()).isEqualTo(-1);
  }

  @Test
  void testWithAge_10() {
    Player p = builder.withAge(10).get();
    assertThat(p.getPerson().getAge()).isEqualTo(10);
  }

  @Test
  void testWithgender_null() {
    Player p = builder.withGender(null).get();
    assertThat(p.getPerson().getGender()).isEqualTo(null);
  }

  @Test
  void testWithgender_Female() {
    Player p = builder.withGender(FEMALE).get();
    assertThat(p.getPerson().getGender()).isEqualTo(FEMALE);
  }
}