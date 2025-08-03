package org.cc.torganizer.core.comparators.player;

import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class PlayerByLastNameComparatorTest {
  PlayerByLastNameComparator comparator;

  static Stream<Arguments> playerProvider() {
    return Stream.of(
      arguments(new Player("", "ln"), new Player("", "ln"), 0),
      arguments(new Player("", "ln1"), new Player("", "ln2"), -1),
      arguments(new Player("", "ln2"), new Player("", "ln1"), 1),
      arguments(new Player("", null), new Player("", null), 0),
      arguments(new Player("", null), new Player("", "ln"), -1),
      arguments(new Player("", "ln"), new Player("", null), 1)
    );
  }

  @BeforeEach
  void beforeEach() {
    comparator = new PlayerByLastNameComparator();
  }

  @ParameterizedTest
  @MethodSource("playerProvider")
  void test(Player player1, Player player2, int expectedCompare) {
    var actualCompare = comparator.compare(player1, player2);

    assertThat(actualCompare).isEqualTo(expectedCompare);
  }
}