/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.core.entities;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author svens
 */
public class OpponentTypeTest {

  @Test
  public void testValueOf_NULL() {
    assertThrows(NullPointerException.class, ()-> {
      OpponentType expResult = null;
      OpponentType result = OpponentType.valueOf(null);
      MatcherAssert.assertThat(expResult, Matchers.is(result));
    });
  }

  @Test
  public void testValueOf_Male() {
    OpponentType expResult = OpponentType.PLAYER;
    OpponentType result = OpponentType.valueOf("PLAYER");
    MatcherAssert.assertThat(expResult, Matchers.is(result));
  }

}
