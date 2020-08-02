package org.cc.torganizer.core.entities.aggregates;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchAggregateTest {

  private MatchAggregate matchAggregate;

  @BeforeEach
  void before() {
    matchAggregate = new MatchAggregate();
  }

  @Test
  void testDraw() {
    Opponent home = new Player(new Person("1", "1"));
    Opponent guest = new Player(new Person("2", "2"));

    Match m = new Match(home, guest);
    m.addResult(new Result(0, 0, 0));

    matchAggregate.aggregate(m, home);
    assertThat(matchAggregate.getRatio()).isEqualTo(0D);
    assertThat(matchAggregate.getWins()).isZero();
    assertThat(matchAggregate.getLose()).isZero();
  }

  @Test
  void testHomeWins() {
    Opponent home = new Player(new Person("1", "1"));
    Opponent guest = new Player(new Person("2", "2"));

    Match m = new Match(home, guest);
    m.addResult(new Result(0, 1, 0));
    m.setFinishedTime(LocalDateTime.now());

    matchAggregate.aggregate(m, home);

    assertThat(matchAggregate.getWins()).isEqualTo(1);
    assertThat(matchAggregate.getLose()).isZero();
    assertThat(matchAggregate.getRatio()).isEqualTo(1D);
  }
}
