package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoundsRepositoryTest extends AbstractDbUnitJpaTest {

  private RoundsRepository repository;

  @BeforeEach
  void beforeAll() throws Exception {
    super.initDatabase("test-data-round.xml");

    repository = new RoundsRepository(entityManager, new DisciplinesRepository(entityManager));
  }

  @Test
  void testCreate() {
    var round = new Round();
    round.setPosition(1);
    round.setQualified(4);
    round.setSystem(System.DOUBLE_ELIMINATION);

    repository.create(round);

    assertThat(round.getId()).isNotNull();
  }

  @Test
  void testReadExisting() {
    var round = repository.read(1L);
    assertThat(round).isNotNull();
  }

  @Test
  void testReadNonExisting() {
    var round = repository.read(100L);
    assertThat(round).isNull();
  }

  @Test
  void testReadMultiple_lessThanAvailable() {
    var rounds = repository.read(0, 1);
    assertThat(rounds).hasSize(1);
  }

  @Test
  void testReadMultiple_moreThanAvailable() {
    var rounds = repository.read(0, 3);
    assertThat(rounds).hasSize(2);
  }

  @Test
  void testGetGroups_existing() {
    var groups = repository.getGroups(1L, 0, 10);

    assertThat(groups).hasSize(4);
  }

  @Test
  void testGetGroups_notExisting() {
    var groups = repository.getGroups(2L, 0, 10);

    assertThat(groups).isEmpty();
  }

  @Test
  void testGetRoundId_existing() {
    // testdata:
    // <_ROUNDS_GROUPS _ROUND_ID="1" _GROUP_ID="3" />
    var id = repository.getRoundId(3L);

    assertThat(id).isEqualTo(1L);
  }

  @Test
  void testGetRoundId_groupDoesNotExist() {
    // testdata:
    // <_ROUNDS_GROUPS _ROUND_ID="1" _GROUP_ID="3" />
    var id = repository.getRoundId(-1L);

    assertThat(id).isNull();
  }

  @Test
  void testGetPosition() {
    var position = repository.getPosition(1L);
    assertThat(position).isEqualTo(1);

    position = repository.getPosition(2L);
    assertThat(position).isEqualTo(2);
  }

  @Test
  void testGetRoundId_ByDisciplineAndPosition() {
    var roundId = repository.getRoundId(1L, 2);
    assertThat(roundId).isEqualTo(2L);

    roundId = repository.getRoundId(1L, 1);
    assertThat(roundId).isEqualTo(1L);
  }

  @Test
  void testGetPreviousRound() {
    var prevRoundId = repository.getPrevRoundId(2L);
    assertThat(prevRoundId).isEqualTo(1L);
  }

  @Test
  void testGetAssignedOpponents_containsData() {
    var assignedOpponents = repository.getAssignedOpponents(1L);
    assertThat(assignedOpponents).hasSize(2);
  }

  @Test
  void testGetAssignedOpponents_Empty() {
    var assignedOpponents = repository.getAssignedOpponents(2L);
    assertThat(assignedOpponents).isEmpty();
  }

  @Test
  void testCreateGroups() {
    var groups = repository.newGroup(1L);

    assertThat(groups).hasSize(5);

    for (var group : groups) {
      assertThat(group.getId()).isNotNull();
    }
  }
}