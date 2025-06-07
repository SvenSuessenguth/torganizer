package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.cc.torganizer.core.entities.aggregates.Aggregation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GroupTest {

  private Group group;

  @BeforeEach
  public void beforeEach() {
    group = new Group();
  }

  @Test
  void testSetPositionalOpponents() {
    List<PositionalOpponent> pos = new ArrayList<>();
    pos.add(new PositionalOpponent(new Player("1", "1"), 1));
    pos.add(new PositionalOpponent(new Player("0", "0"), 0));

    assertThat(group.getPositionalOpponents()).isEmpty();

    group.setPositionalOpponents(pos);

    assertThat(group.getPositionalOpponents()).hasSize(2);
    assertThat(group.getPositionalOpponents().getFirst().getPosition()).isZero();
  }

  @Test
  void testRemoveOpponent_null() {
    List<Opponent> opponentsBefore = group.getOpponents();
    group.removeOpponent(null);
    List<Opponent> opponentsAfter = group.getOpponents();

    assertThat(opponentsAfter).containsOnlyOnceElementsOf(opponentsBefore);
  }

  @Test
  void testRemoveOpponent_reindex() {
    Player playerToRemove = new Player();
    Player testPlayer = new Player();

    group.addOpponent(new Player());
    group.addOpponent(playerToRemove);
    group.addOpponent(testPlayer);

    int indexBefore = group.getPositionalOpponent(testPlayer).getPosition();
    group.removeOpponent(playerToRemove);
    int indexAfter = group.getPositionalOpponent(testPlayer).getPosition();
    List<Opponent> opponentsAfter = group.getOpponents();

    assertThat(opponentsAfter).hasSize(2);
    assertThat(indexBefore).isNotEqualTo(indexAfter);
  }

  @Test
  void testGetPositionalOpponent_null() {
    PositionalOpponent positionalOpponent = group.getPositionalOpponent((Opponent) null);

    assertThat(positionalOpponent).isNull();
  }

  @Test
  void testSetOpponents_null() {
    group.setOpponents(null);
    List<PositionalOpponent> positionalOpponents = group.getPositionalOpponents();
    assertThat(positionalOpponents).isEmpty();
  }

  @Test
  void testSetOpponents_empty() {
    group.setOpponents(new ArrayList<>());
    List<PositionalOpponent> positionalOpponents = group.getPositionalOpponents();
    assertThat(positionalOpponents).isEmpty();
  }

  @Test
  void testSetOpponents() {
    group.setOpponents(Arrays.asList(new Player(), new Player()));
    List<PositionalOpponent> positionalOpponents = group.getPositionalOpponents();
    assertThat(positionalOpponents).hasSize(2);
  }

  @Test
  void testGetOpponent_null() {
    Opponent opponent = group.getOpponent(null);
    assertThat(opponent).isNull();
  }

  @Test
  void testGetOpponent_invalid() {
    Opponent opponent = group.getOpponent(Integer.MIN_VALUE);
    assertThat(opponent).isNull();
  }

  @Test
  void testGetOpponent_valid() {
    Player expected = new Player();
    group.addOpponent(expected);
    Opponent opponent = group.getOpponent(0);
    assertThat(opponent).isNotNull().isEqualTo(expected);
  }

  @Test
  void testGetAggregates_empty() {
    List<Aggregation> aggregates = group.getAggregates();
    assertThat(aggregates).isEmpty();
  }

  @Test
  void testGetAggregates_minimal() {
    // two players with one match
    Player p1 = new Player();
    Player p2 = new Player();
    group.addOpponent(p1);
    group.addOpponent(p2);

    Match m = new Match(p1, p2);
    m.addResult(new Result(0, 0, 1));
    group.getMatches().add(m);

    List<Aggregation> aggregates = group.getAggregates();
    assertThat(aggregates).isNotEmpty().hasSize(2);
    assertThat(aggregates.getFirst().getOpponent()).isEqualTo(p2);
  }

  @SuppressWarnings("unused")
  public static Stream<Arguments> getFinishedMatches() {
    return Stream.of(
        Arguments.of(null, 0),
        Arguments.of(List.of(), 0),
        Arguments.of(List.of(runningMatch(), idleMatch()), 0),
        Arguments.of(List.of(finishedMatch(), finishedMatch(), runningMatch(), idleMatch(), finishedMatch()), 3)
    );
  }

  @ParameterizedTest
  @MethodSource
  void getFinishedMatches(List<Match> matches, int expected) {
    Group g = new Group();
    g.setMatches(matches);

    List<Match> finishedMatches = g.getFinishedMatches();

    assertThat(finishedMatches).hasSize(expected);
  }

  @SuppressWarnings("unused")
  public static Stream<Arguments> getRunningMatches() {
    return Stream.of(
        Arguments.of(null, 0),
        Arguments.of(List.of(), 0),
        Arguments.of(List.of(runningMatch(), idleMatch()), 1),
        Arguments.of(List.of(finishedMatch(), finishedMatch(), runningMatch(), idleMatch(), finishedMatch()), 1)
    );
  }

  @ParameterizedTest
  @MethodSource
  void getRunningMatches(List<Match> matches, int expected) {
    Group g = new Group();
    g.setMatches(matches);

    List<Match> runningMatches = g.getRunningMatches();

    assertThat(runningMatches).hasSize(expected);
  }

  public static Stream<Arguments> getMatch() {
    return Stream.of(
        Arguments.of(List.of(finishedMatch(), finishedMatch(), runningMatch(), idleMatch(), finishedMatch()), 0, true),
        Arguments.of(List.of(finishedMatch(), finishedMatch(), runningMatch(), idleMatch(), finishedMatch()), 4, true),
        Arguments.of(List.of(finishedMatch(), finishedMatch(), runningMatch(), idleMatch(), finishedMatch()), 6, false),
        Arguments.of(List.of(finishedMatch(), finishedMatch(), runningMatch(), idleMatch(), finishedMatch()), -1, false),
        Arguments.of(List.of(), 0, false),
        Arguments.of(null, 0, false)

    );
  }

  @ParameterizedTest
  @MethodSource
  void getMatch(List<Match> matches, int position, boolean matchFound) {
    if (matches != null) {
      IntStream.range(0, matches.size())
          .forEach(idx -> matches.get(idx).setPosition(idx));
    }
    Group g = new Group();
    g.setMatches(matches);

    Match match = g.getMatch(position);
    assertThat(match != null).isEqualTo(matchFound);
  }


  private static Match finishedMatch() {
    Match m = new Match();
    m.setRunning(false);
    m.setFinishedTime(LocalDateTime.now());
    return m;
  }

  private static Match runningMatch() {
    Match m = new Match();
    m.setRunning(true);
    return m;
  }

  private static Match idleMatch() {
    Match m = new Match();
    m.setRunning(false);
    return m;
  }
}