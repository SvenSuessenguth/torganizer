package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.MALE;

import org.junit.jupiter.api.Test;

/**
 * @author svens
 */
class GenderTest {

  @Test
  void testValueOf_UNKNOWN() {
    String name = "UNKNOWN";
    Gender result = Gender.valueOf(name);
    assertThat(result).isNotNull();
  }

  @Test
  void testGetId() {
    String id = MALE.getId();

    assertThat(id).isEqualTo("M");
  }

  @Test
  void testById_M() {
    Gender gender = Gender.byId("M");

    assertThat(gender).isEqualTo(MALE);
  }

  @Test
  void testById_NULL() {
    Gender gender = Gender.byId(null);

    assertThat(gender).isNull();
  }
}
