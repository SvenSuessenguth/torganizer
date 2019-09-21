package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DisciplinesRepositoryTest extends AbstractDbUnitJpaTest {

  private DisciplinesRepository repository;

  @BeforeEach
  void before() throws Exception {
    super.initDatabase("test-data-tournament.xml");

    repository = new DisciplinesRepository(entityManager);
  }

  @Test
  void testGetOpponents() {
    List<Opponent> opponents = repository.getOpponents(1L, null, null);

    assertThat(opponents).isNotNull();
    assertThat(opponents).hasSize(2);
  }

  @Test
  void testGetDisciplineId_existing(){
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="3" />
    Long id = repository.getDisciplineId(3L);

    assertThat(id).isEqualTo(1L);
  }

  @Test
  void testGetRoundId_roundDoesNotExist(){
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    Long id = repository.getDisciplineId(-1L);

    assertThat(id).isNull();
  }

  @Test
  void testGetRoundByPosition_roundExisting(){
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    Round round = repository.getRoundByPosition(1L, 2);

    assertThat(round).isNotNull();
  }

  @Test
  void testGetRoundByPosition_roundNotExisting(){
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    Round round = repository.getRoundByPosition(1L, 4);

    assertThat(round).isNull();
  }

  @Test
  void testGetRoundByPosition_disciplineNotExisting(){
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    Round round = repository.getRoundByPosition(2L, 2);

    assertThat(round).isNull();
  }
}
