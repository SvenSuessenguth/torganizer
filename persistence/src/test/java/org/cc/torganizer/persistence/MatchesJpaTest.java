package org.cc.torganizer.persistence;

import org.junit.jupiter.api.BeforeEach;

public class MatchesJpaTest extends AbstractDbUnitJpaTest {

  @BeforeEach
  public void before() throws Exception {
    super.initDatabase("test-data-matches.xml");
  }
}
