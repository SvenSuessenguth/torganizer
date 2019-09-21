package org.cc.torganizer.core.entities;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
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
