package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RoundsRepositoryTest extends AbstractDbUnitJpaTest {

  private RoundsRepository repository;

  @BeforeEach
  public void before() throws Exception {
    super.initDatabase("test-data-round.xml");

    repository = new RoundsRepository(entityManager);
  }

  @Test
  public void testCreate(){
    Round round = new Round();
    round.setPosition(1);
    round.setQualified(4);
    round.setSystem(System.DOUBLE_ELIMINATION);

    repository.create(round);

    assertThat(round.getId(), is(not(nullValue())));
  }

  @Test
  public void testReadExisting(){
    Round round = repository.read(1L);
    assertThat(round, is(not(nullValue())));
  }

  @Test
  public void testReadNonExisting(){
    Round round = repository.read(100L);
    assertThat(round, is(nullValue()));
  }

  @Test
  public void testReadMultiple_lessThanAvailable(){
    List<Round> rounds = repository.read(0, 1);
    assertThat(rounds, is(not(nullValue())));
    assertThat(rounds, hasSize(1));
  }

  @Test
  public void testReadMultiple_moreThanAvailable(){
    List<Round> rounds = repository.read(0, 3);
    assertThat(rounds, is(not(nullValue())));
    assertThat(rounds, hasSize(2));
  }

  @Test
  public void testGetGroups_existing(){
    List<Group> groups = repository.getGroups(1L, 0, 10);

    assertThat(groups, hasSize(4));
  }

  @Test
  public void testGetGroups_notExisting(){
    List<Group> groups = repository.getGroups(2L, 0, 10);

    assertThat(groups, is(empty()));
  }

  @Test
  public void testGetRoundId_existing(){
    // testdata:
    // <_ROUNDS_GROUPS _ROUND_ID="1" _GROUP_ID="3" />
    Long id = repository.getRoundId(3L);

    assertThat(id, is(1L));
  }

  @Test
  public void testGetRoundId_groupDoesNotExist(){
    // testdata:
    // <_ROUNDS_GROUPS _ROUND_ID="1" _GROUP_ID="3" />
    Long id = repository.getRoundId(-1L);

    assertThat(id, is(nullValue()));
  }
}