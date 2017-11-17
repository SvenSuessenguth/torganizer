package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.cc.torganizer.core.entities.Opponent;
import org.junit.Before;
import org.junit.Test;

public class OpponentsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-opponents.xml");
  }

  @Test
  public void testFindAll() {
    List<Opponent> opponents = entityManager.createNamedQuery("Opponent.findAll", Opponent.class)
        .getResultList();
    assertThat(opponents, hasSize(2));
  }
}
