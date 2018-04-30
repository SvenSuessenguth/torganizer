package org.cc.torganizer.persistence;

import java.util.List;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Player;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.Before;
import org.junit.Test;

public class MatchesJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-matches.xml");
  }
}
