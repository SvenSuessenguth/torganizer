package org.cc.torganizer.persistence;

import org.junit.Before;

public class GroupsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-groups.xml");
  }
}
