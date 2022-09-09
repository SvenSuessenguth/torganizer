package org.cc.torganizer.core.entities.aggregates;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ResultAggregateTest {

  private ResultAggregate resultAggregate;

  @BeforeEach
  public void beforeEach() {
    resultAggregate = new ResultAggregate();
  }

  public static Stream<Arguments> aggregate() {
    return Stream.of(
        Arguments.of(m(List.of(r(1, 1, 0), r(2, 1, 0))), 2, 0),
        Arguments.of(m(List.of(r(1, 0, 1), r(2, 0, 1))), 0, 2),
        Arguments.of(m(List.of(r(1, 1, 0), r(2, 0, 1))), 1, 1)
    );
  }

  @ParameterizedTest
  @MethodSource("aggregate")
  void aggregateHome(Match match, Integer expectedWins, Integer expectedLose) {
    resultAggregate.aggregate(match, match.getHome());

    assertThat(resultAggregate.getWins()).isEqualTo(expectedWins);
    assertThat(resultAggregate.getLose()).isEqualTo(expectedLose);
  }

  @ParameterizedTest
  @MethodSource("aggregate")
  void aggregateGuest(Match match, Integer expectedWins, Integer expectedLose) {
    resultAggregate.aggregate(match, match.getGuest());

    assertThat(resultAggregate.getWins()).isEqualTo(expectedLose);
    assertThat(resultAggregate.getLose()).isEqualTo(expectedWins);
  }

  public static Match m(List<Result> results) {
    var match = new Match(new Player("1", "1"), new Player("2", "2"));
    results.forEach(match::addResult);

    return match;
  }

  public static Result r(Integer position, Integer homeScore, Integer guestScore) {
    return new Result(position, homeScore, guestScore);
  }


}