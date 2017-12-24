package org.cc.torganizer.core.entities;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 *
 * @author svens
 */
public class GenderTest {
  
  @Test
  public void testValueOf_UNKNOWN() {
    String name = "UNKNOWN";
    Gender result = Gender.valueOf(name);
    MatcherAssert.assertThat(result, Matchers.is(Matchers.notNullValue()));    
  }
  
}
