package org.cc.torganizer.core.entities.aggregates;

import org.cc.torganizer.core.entities.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MatchAggregateTest {

  private MatchAggregate matchAggregate;

  @BeforeEach
  public void before(){
    matchAggregate = new MatchAggregate();
  }

  @Test
  public void testDraw(){
    Opponent home = new Player(new Person("1", "1"));
    Opponent guest = new Player(new Person("2", "2"));

    Match m = new Match(home, guest);
    m.addResult(new Result(0, 0, 0));

    matchAggregate.aggregate(m, home);
    assertThat(matchAggregate.getRatio(), is(0D));
    assertThat(matchAggregate.getWins(), is(0));
    assertThat(matchAggregate.getLose(), is(0));
  }

  @Test
  public void testHomeWins(){
    Opponent home = new Player(new Person("1", "1"));
    Opponent guest = new Player(new Person("2", "2"));

    Match m = new Match(home, guest);
    m.addResult(new Result(0, 1, 0));
    m.setFinishedTime(LocalDateTime.now());

    matchAggregate.aggregate(m, home);

    assertThat("invalid wins", matchAggregate.getWins(), is(1));
    assertThat("invalid lose", matchAggregate.getLose(), is(0));
    assertThat("invalid ratio", matchAggregate.getRatio(), is(1D) );
  }
}
