package org.cc.torganizer.core.filter;

import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Restriction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.Gender.UNKNOWN;
import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.SQUAD;

class OpponentFilterTest {

  OpponentFilter filter;

  @SuppressWarnings("unused")
  private static Stream<Arguments> pass() {
    // Opponent, Restrictions, expected
    return Stream.of(
      // player pass no restriction
      Arguments.of(player(10, MALE), List.of(), 1),
      Arguments.of(
        player(10, MALE),
        List.of(new GenderRestriction(MALE), new AgeRestriction(9, 11)),
        1),
      Arguments.of(
        player(10, MALE),
        List.of(new GenderRestriction(MALE), new AgeRestriction(9, 11), new OpponentTypeRestriction(SQUAD)),
        0),
      Arguments.of(
        player(10, MALE),
        List.of(new GenderRestriction(FEMALE), new AgeRestriction(9, 11), new OpponentTypeRestriction(PLAYER)),
        0),
      Arguments.of(
        player(10, MALE),
        List.of(new GenderRestriction(UNKNOWN), new AgeRestriction(), new OpponentTypeRestriction(PLAYER)),
        1)
    );
  }

  @BeforeEach
  void beforeEach() {
    filter = new OpponentFilter();
  }

  @ParameterizedTest
  @MethodSource("pass")
  void pass(Opponent opponent, Collection<Restriction> restrictions, int expected) {

    var pass = filter.pass(List.of(opponent), restrictions);

    assertThat(pass).size().isEqualTo(expected);
  }

  private static Player player(Integer age, Gender gender) {
    var player = Player.builder().build();
    var person = player.getPerson();
    person.setDateOfBirth(LocalDate.now().minusYears(age));
    person.setGender(gender);

    return player;
  }
}