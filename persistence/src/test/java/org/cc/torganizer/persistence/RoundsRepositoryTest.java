package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoundsRepositoryTest extends AbstractDbUnitJpaTest {

  private RoundsRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-round.xml");

    repository = new RoundsRepository(entityManager, new DisciplinesRepository(entityManager));
  }

  @Test
  void testCreate() {
    Round round = new Round();
    round.setPosition(1);
    round.setQualified(4);
    round.setSystem(System.DOUBLE_ELIMINATION);

    repository.create(round);

    assertThat(round.getId()).isNotNull();
  }

  @Test
  void testReadExisting() {
    Round round = repository.read(1L);
    assertThat(round).isNotNull();
  }

  @Test
  void testReadNonExisting() {
    Round round = repository.read(100L);
    assertThat(round).isNull();
  }

  @Test
  void testReadMultiple_lessThanAvailable() {
    List<Round> rounds = repository.read(0, 1);
    assertThat(rounds).hasSize(1);
  }

  @Test
  void testReadMultiple_moreThanAvailable() {
    List<Round> rounds = repository.read(0, 3);
    assertThat(rounds).hasSize(2);
  }

  @Test
  void testGetGroups_existing() {
    List<Group> groups = repository.getGroups(1L, 0, 10);

    assertThat(groups).hasSize(4);
  }

  @Test
  void testGetGroups_notExisting() {
    List<Group> groups = repository.getGroups(2L, 0, 10);

    assertThat(groups).isEmpty();
  }

  @Test
  void testGetRoundId_existing() {
    // testdata:
    // <_ROUNDS_GROUPS _ROUND_ID="1" _GROUP_ID="3" />
    Long id = repository.getRoundId(3L);

    assertThat(id).isEqualTo(1L);
  }

  @Test
  void testGetRoundId_groupDoesNotExist() {
    // testdata:
    // <_ROUNDS_GROUPS _ROUND_ID="1" _GROUP_ID="3" />
    Long id = repository.getRoundId(-1L);

    assertThat(id).isNull();
  }

  @Test
  void testGetPosition() {
    Integer position = repository.getPosition(1L);
    assertThat(position).isEqualTo(1);

    position = repository.getPosition(2L);
    assertThat(position).isEqualTo(2);
  }

  @Test
  void testGetRoundId_ByDisciplineAndPosition() {
    Long roundId = repository.getRoundId(1L, 2);
    assertThat(roundId).isEqualTo(2L);

    roundId = repository.getRoundId(1L, 1);
    assertThat(roundId).isEqualTo(1L);
  }

  @Test
  void testGetPreviousRound() {
    Long prevRoundId = repository.getPrevRoundId(2L);
    assertThat(prevRoundId).isEqualTo(1L);
  }

  @Test
  void testGetAssignedOpponents_containsData() {
    Set<Opponent> assignedOpponents = repository.getAssignedOpponents(1L);
    assertThat(assignedOpponents).hasSize(2);
  }

  @Test
  void testGetAssignedOpponents_Empty() {
    Set<Opponent> assignedOpponents = repository.getAssignedOpponents(2L);
    assertThat(assignedOpponents).isEmpty();
  }

  @Test
  void testCreateGroups() {
    List<Group> groups = repository.newGroup(1L);

    assertThat(groups).hasSize(5);

    for (Group group : groups) {
      assertThat(group.getId()).isNotNull();
    }
  }
}