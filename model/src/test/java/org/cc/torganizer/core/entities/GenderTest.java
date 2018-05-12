package org.cc.torganizer.core.entities;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 * @author svens
 */
public class GenderTest {
  
  @Test
  public void testValueOf_UNKNOWN() {
    String name = "UNKNOWN";
    Gender result = Gender.valueOf(name);
    assertThat(result, Matchers.is(Matchers.notNullValue()));
  }
  
}
