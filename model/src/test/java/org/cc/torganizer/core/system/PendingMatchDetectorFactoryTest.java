package org.cc.torganizer.core.system;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PendingMatchDetectorFactoryTest {
  PendingMatchDetectorFactory factory;

  @BeforeEach
  public void before() {
    factory = new PendingMatchDetectorFactory();
  }

  @AfterEach
  public void after() {
    factory = null;
  }

  @Test
  public void testGetMatchMaker() {

    for (org.cc.torganizer.core.entities.System system : org.cc.torganizer.core.entities.System.values()) {
      assertThat(factory.getPendingMatchDetector(system, null), is(not(nullValue())));
    }
  }

  @Test
  public void testGetMatchMakerNull() {
    assertThrows(IllegalArgumentException.class, ()-> {
      factory.getPendingMatchDetector(null, null);
    });
  }
}
