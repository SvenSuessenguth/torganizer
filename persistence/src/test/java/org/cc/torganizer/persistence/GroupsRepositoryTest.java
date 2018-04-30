package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Group;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class GroupsRepositoryTest extends AbstractDbUnitJpaTest {

  private GroupsRepository repository;

  @Before
  public void before() throws Exception {
    super.initDatabase("test-data-groups.xml");
    repository = new GroupsRepository(entityManager);
  }

  @Test
  public void testRead_null(){
    Group g = repository.read(2L);
    assertThat(g, is(nullValue()));
  }

  @Test
  public void testRead_notNull(){
    Group g = repository.read(1L);
    assertThat(g, is(not(nullValue())));
  }

  @Test
  public void testCount(){
    long count = repository.count();
    assertThat(count, is(1L));
  }
}
