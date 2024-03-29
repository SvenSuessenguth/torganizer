package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DisciplinesRepositoryTest extends AbstractDbUnitJpaTest {

  private DisciplinesRepository repository;
  private RestrictionsRepository restrictionsRepository;
  private RoundsRepository roundsRepository;

  @BeforeEach
  public void beforeEach() throws Exception {
    super.initDatabase("test-data-tournament.xml");

    repository = new DisciplinesRepository(entityManager);
    restrictionsRepository = new RestrictionsRepository(entityManager);
    roundsRepository = new RoundsRepository(entityManager, repository);
  }

  @Test
  void testGetOpponents() {
    List<Opponent> opponents = repository.getOpponents(1L, null, null);

    assertThat(opponents).hasSize(2);
  }

  @Test
  void testGetDisciplineId_existing() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="3" />
    Long id = repository.getDisciplineId(3L);

    assertThat(id).isEqualTo(1L);
  }

  @Test
  void testGetRoundId_roundDoesNotExist() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    Long id = repository.getDisciplineId(-1L);

    assertThat(id).isNull();
  }

  @Test
  void testGetRoundByPosition_roundExisting() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    Round round = repository.getRoundByPosition(1L, 2);

    assertThat(round).isNotNull();
  }

  @Test
  void testGetRoundByPosition_roundNotExisting() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    Round round = repository.getRoundByPosition(1L, 4);

    assertThat(round).isNull();
  }

  @Test
  void testGetRoundByPosition_disciplineNotExisting() {
    // testdata:
    // <_DISCIPLINES_ROUNDS _DISCIPLINE_ID="1" _ROUND_ID="1" />
    Round round = repository.getRoundByPosition(2L, 2);

    assertThat(round).isNull();
  }

  @Test
  void testCreate() {
    Discipline discipline = new Discipline();
    discipline.setName("test");

    GenderRestriction genderRestriction = new GenderRestriction();
    genderRestriction.setGender(Gender.FEMALE);

    OpponentTypeRestriction opponentTypeRestriction = new OpponentTypeRestriction();
    opponentTypeRestriction.setOpponentType(OpponentType.SQUAD);

    AgeRestriction ageRestriction = new AgeRestriction();
    ageRestriction.setMaxDateOfBirth(LocalDate.now());
    ageRestriction.setMinDateOfBirth(LocalDate.now().minusYears(1));

    restrictionsRepository.create(ageRestriction);
    restrictionsRepository.create(genderRestriction);
    restrictionsRepository.create(opponentTypeRestriction);

    discipline.addRestriction(genderRestriction);
    discipline.addRestriction(opponentTypeRestriction);
    discipline.addRestriction(ageRestriction);

    long countBefore = repository.count();
    repository.create(discipline);
    long countAfter = repository.count();

    assertThat(countBefore).isEqualTo(countAfter - 1);
    assertThat(discipline.getId()).isNotNull();

    assertThat(genderRestriction.getId()).isNotNull();
    assertThat(opponentTypeRestriction.getId()).isNotNull();
    assertThat(ageRestriction.getId()).isNotNull();
  }

  @Test
  void testPersistNewRound() {
    Discipline discipline = repository.read(1L);
    int roundsCount = discipline.getRounds().size();

    Round round = new Round();
    roundsRepository.create(round);
    discipline.addRound(round);
    repository.update(discipline);

    assertThat(discipline).isNotNull();
    assertThat(round.getId()).isNotNull();

    Discipline discipline2 = repository.read(1L);
    int roundsCount2 = discipline2.getRounds().size();

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
    List<Round> rounds = repository.getRounds(1L, offset, maxResults);

    assertThat(rounds).hasSize(count);
  }
}
