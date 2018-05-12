package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GroupsRepositoryTest extends AbstractDbUnitJpaTest {

  private GroupsRepository repository;

  @BeforeEach
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
