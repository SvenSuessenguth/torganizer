package org.cc.torganizer.core.filter;

import org.cc.torganizer.core.builder.PlayerBuilder;
import org.cc.torganizer.core.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.*;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.SQUAD;

class OpponentFilterTest {

  OpponentFilter filter;

  @SuppressWarnings("unused")
  private static Stream<Arguments> addMatch() {
    PlayerBuilder playerBuilder = new PlayerBuilder();

    // Opponent, Restrictions, expected
    return Stream.of(
      // player pass no restriction
      Arguments.of(
        playerBuilder.standard().withAge(10).withGender(MALE).get(),
        asList(),
        1),
      Arguments.of(
        playerBuilder.standard().withAge(10).withGender(MALE).get(),
        asList(new GenderRestriction(MALE), new AgeRestriction(9, 11)),
        1),
      Arguments.of(
        playerBuilder.standard().withAge(10).withGender(MALE).get(),
        asList(new GenderRestriction(MALE), new AgeRestriction(9, 11), new OpponentTypeRestriction(SQUAD)),
        0),
      Arguments.of(
        playerBuilder.standard().withAge(10).withGender(MALE).get(),
        asList(new GenderRestriction(FEMALE), new AgeRestriction(9, 11), new OpponentTypeRestriction(PLAYER)),
        0),
      Arguments.of(
        playerBuilder.standard().withAge(10).withGender(MALE).get(),
        asList(new GenderRestriction(UNKNOWN), new AgeRestriction(), new OpponentTypeRestriction(PLAYER)),
        1)
    );
  }

  @BeforeEach
  public void beforeEach() {
    filter = new OpponentFilter();
  }

  @ParameterizedTest
  @MethodSource("addMatch")
  void testFilter(Opponent opponent, Collection<Restriction> restrictions, int expected) {

    Collection<Opponent> pass = filter.pass(asList(opponent), restrictions);

    assertThat(pass).size().isEqualTo(expected);
  }
}