package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.cc.torganizer.core.entities.Result;
import org.junit.Before;
import org.junit.Test;

public class ResultsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-results.xml");
  }

  @Test
  public void testFindAll() {
    List<Result> results = entityManager.createNamedQuery("Result.findAll", Result.class).getResultList();
    assertThat(results, hasSize(3));
  }
}
