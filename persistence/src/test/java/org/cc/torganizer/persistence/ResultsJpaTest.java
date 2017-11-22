package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.cc.torganizer.core.entities.Result;
import org.hamcrest.Matchers;
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
  
  @Test
  public void testFindById() {
    List<Result> results = entityManager.createNamedQuery("Result.findById", Result.class).setParameter("id", 3L).getResultList();
    assertThat(results, hasSize(1));
    Result r = results.get(0);
    assertThat(r.isDraw(), is(true));
    assertThat(r.getPosition(), is(3));
  }
}
