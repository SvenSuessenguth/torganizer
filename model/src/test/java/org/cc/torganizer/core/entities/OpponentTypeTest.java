/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.core.entities;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 *
 * @author svens
 */
public class OpponentTypeTest {

  public OpponentTypeTest() {
  }

  @Test(expected = NullPointerException.class)
  public void testValueOf_NULL() {
    OpponentType expResult = null;
    OpponentType result = OpponentType.valueOf(null);
    MatcherAssert.assertThat(expResult, Matchers.is(result));
  }

  @Test
  public void testValueOf_Male() {
    OpponentType expResult = OpponentType.PLAYER;
    OpponentType result = OpponentType.valueOf("PLAYER");
    MatcherAssert.assertThat(expResult, Matchers.is(result));
  }

}
