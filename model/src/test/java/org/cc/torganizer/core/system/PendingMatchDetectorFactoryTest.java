package org.cc.torganizer.core.system;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PendingMatchDetectorFactoryTest {
  PendingMatchDetectorFactory factory;

  @Before
  public void before() {
    factory = new PendingMatchDetectorFactory();
  }

  @After
  public void after() {
    factory = null;
  }

  @Test
  public void testGetMatchMaker() {

    for (org.cc.torganizer.core.entities.System system : org.cc.torganizer.core.entities.System.values()) {
      assertNotNull(factory.getPendingMatchDetector(system, null));
    }
  }

  @Test(expected = AssertionError.class)
  public void testGetMatchMakerNull() {
    factory.getPendingMatchDetector(null, null);
  }
}
