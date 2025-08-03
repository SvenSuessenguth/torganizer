package org.cc.torganizer.persistence;

import org.cc.torganizer.core.entities.Tournament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchesRepositoryTest extends AbstractDbUnitJpaTest {

  private MatchesRepository repository;

  @BeforeEach
  void beforeAll() throws Exception {
    super.initDatabase("test-data-matches.xml");
    repository = new MatchesRepository(entityManager);
  }

  @Test
  void read_existing() {
    var match = repository.read(1L);
    assertThat(match).isNotNull();
  }

  @Test
  void read_notExisting() {
    var match = repository.read(999L);
    assertThat(match).isNull();
  }

  @Test
  void read_inRange() {
    var matches = repository.read(0, 10);
    assertThat(matches).hasSize(2);
  }

  @Test
  void read_outRange() {
    var matches = repository.read(10, 10);
    assertThat(matches).isEmpty();
  }

  @Test
  void read_defaultRange() {
    var matches = repository.read(null, null);
    assertThat(matches).hasSize(2);
  }

  @Test
  void count() {
    var count = repository.count();
    assertThat(count).isEqualTo(2);
  }

  @Test
  void getRunningMatches() {
    var runningMatches = repository.getRunningMatches(new Tournament(1L));
    assertThat(runningMatches).isNotEmpty().hasSize(1);
  }

  @Test
  void getFinishedMatches() {
    var finishedMatches = repository.getFinishedMatches(new Tournament(1L));
    assertThat(finishedMatches).isNotEmpty().hasSize(1);
  }

  @Test
  void getRunningMatches_invalidTournament() {
    var runningMatches = repository.getRunningMatches(new Tournament(2L));
    assertThat(runningMatches).isEmpty();
  }

  @Test
  void getFinishedMatches_invalidTournament() {
    var finishedMatches = repository.getFinishedMatches(new Tournament(2L));
    assertThat(finishedMatches).isEmpty();
  }
}