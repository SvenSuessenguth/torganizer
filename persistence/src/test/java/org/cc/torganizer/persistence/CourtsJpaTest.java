package org.cc.torganizer.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import javax.persistence.TypedQuery;

import org.cc.torganizer.core.entities.Court;
import org.junit.Before;
import org.junit.Test;

public class CourtsJpaTest extends AbstractDbUnitJpaTest {

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-courts.xml");
  }
  
  @Test
  public void testFindById_exists() {
    TypedQuery<Court> namedQuery = entityManager.createNamedQuery("Court.findById", Court.class);
    namedQuery.setParameter("id", 1L);
    
    List<Court> allCourts = namedQuery.getResultList();
    assertThat(allCourts, hasSize(1));
  }
  
  @Test
  public void testFindAll() {
    TypedQuery<Court> namedQuery = entityManager.createNamedQuery("Court.findAll", Court.class);
    
    List<Court> allCourts = namedQuery.getResultList();
    assertThat(allCourts, hasSize(2));
  }

}
