package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

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
}
