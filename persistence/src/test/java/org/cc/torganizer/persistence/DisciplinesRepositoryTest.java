package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DisciplinesRepositoryTest extends AbstractDbUnitJpaTest {

  private DisciplinesRepository repository;
  private RestrictionsRepository restrictionsRepository;
  private RoundsRepository roundsRepository;

  @BeforeEach
  void beforeEach() throws Exception {
    super.initDatabase("test-data-tournament.xml");

    repository = new DisciplinesRepository(entityManager);
    restrictionsRepository = new RestrictionsRepository(entityManager);
    roundsRepository = new RoundsRepository(entityManager, repository);
  }

  @Test
  void testGetOpponents() {
    var opponents = repository.getOpponents(1L, null, null);

    assertThat(opponents).hasSize(2);
  }

  @Test
  void testGetDisciplineId_existing() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="3" />
    var id = repository.getDisciplineId(3L);

    assertThat(id).isEqualTo(1L);
  }

  @Test
  void testGetRoundId_roundDoesNotExist() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    var id = repository.getDisciplineId(-1L);

    assertThat(id).isNull();
  }

  @Test
  void testGetRoundByPosition_roundExisting() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    var round = repository.getRoundByPosition(1L, 2);

    assertThat(round).isNotNull();
  }

  @Test
  void testGetRoundByPosition_roundNotExisting() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    var round = repository.getRoundByPosition(1L, 4);

    assertThat(round).isNull();
  }

  @Test
  void testGetRoundByPosition_disciplineNotExisting() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    var round = repository.getRoundByPosition(2L, 2);

    assertThat(round).isNull();
  }

  @Test
  void testCreate() {
    var discipline = new Discipline();
    discipline.setName("test");

    var genderRestriction = new GenderRestriction();
    genderRestriction.setGender(Gender.FEMALE);

    var opponentTypeRestriction = new OpponentTypeRestriction();
    opponentTypeRestriction.setOpponentType(OpponentType.SQUAD);

    var ageRestriction = new AgeRestriction();
    ageRestriction.setMaxDateOfBirth(LocalDate.now());
    ageRestriction.setMinDateOfBirth(LocalDate.now().minusYears(1));

    restrictionsRepository.create(ageRestriction);
    restrictionsRepository.create(genderRestriction);
    restrictionsRepository.create(opponentTypeRestriction);

    discipline.addRestriction(genderRestriction);
    discipline.addRestriction(opponentTypeRestriction);
    discipline.addRestriction(ageRestriction);

    var countBefore = repository.count();
    repository.create(discipline);
    var countAfter = repository.count();

    assertThat(countBefore).isEqualTo(countAfter - 1);
    assertThat(discipline.getId()).isNotNull();

    assertThat(genderRestriction.getId()).isNotNull();
    assertThat(opponentTypeRestriction.getId()).isNotNull();
    assertThat(ageRestriction.getId()).isNotNull();
  }

  @Test
  void testPersistNewRound() {
    var discipline = repository.read(1L);
    var roundsCount = discipline.getRounds().size();

    var round = new Round();
    roundsRepository.create(round);
    discipline.addRound(round);
    repository.update(discipline);

    assertThat(discipline).isNotNull();
    assertThat(round.getId()).isNotNull();

    var discipline2 = repository.read(1L);
    var roundsCount2 = discipline2.getRounds().size();

    assertThat(roundsCount).isLessThan(roundsCount2);
  }

  public static Stream<Arguments> getRounds() {
    return Stream.of(
      Arguments.of(null, null, 3),
      Arguments.of(null, 10, 3),
      Arguments.of(0, null, 3),
      Arguments.of(0, 3, 3),
      Arguments.of(0, 1, 1),
      Arguments.of(1, 2, 2),
      Arguments.of(5, 1, 0)
    );
  }

  @ParameterizedTest
  @MethodSource
  void getRounds(Integer offset, Integer maxResults, Integer count) {
    var rounds = repository.getRounds(1L, offset, maxResults);

    assertThat(rounds).hasSize(count);
  }
}
