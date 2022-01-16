package org.cc.torganizer.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.cc.torganizer.core.entities.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchesRepositoryTest extends AbstractDbUnitJpaTest {

  private MatchesRepository repository;

  @BeforeEach
  public void beforeAll() throws Exception {
    super.initDatabase("test-data-matches.xml");
    repository = new MatchesRepository(entityManager);
  }

  @Test
  void read_existing() {
    Match match = repository.read(1L);
    assertThat(match).isNotNull();
  }

  @Test
  void read_notExisting() {
    Match match = repository.read(2L);
    assertThat(match).isNull();
  }

  @Test
  void read_inRange() {
    List<Match> matches = repository.read(0, 10);
    assertThat(matches).hasSize(1);
  }

  @Test
  void read_outRange() {
    List<Match> matches = repository.read(10, 10);
    assertThat(matches).isEmpty();
  }

  @Test
  void read_defaultRange() {
    List<Match> matches = repository.read(null, null);
    assertThat(matches).hasSize(1);
  }

  @Test
  void count() {
    long count = repository.count();
    assertThat(count).isEqualTo(1);
  }
}