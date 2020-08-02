/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * @author svens
 */
class OpponentTypeTest {

  @Test
  void testValueOf_NULL() {
    assertThrows(NullPointerException.class, () -> {
      OpponentType result = OpponentType.valueOf(null);

      assertThat(result).isNull();
    });
  }

  @Test
  void testValueOf_Male() {
    OpponentType expResult = OpponentType.PLAYER;
    OpponentType result = OpponentType.valueOf("PLAYER");

    assertThat(expResult).isEqualTo(result);
  }
}
