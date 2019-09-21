package org.cc.torganizer.core.entities;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

class AgeRestrictionTest {

  @SuppressWarnings("unused")
  private static Stream<Arguments> addRestrictionAndPlayer() {
    // maxAgeOfBirth, minAgeOfBirth, dateOfBirth, expectedRestriction

    return Stream.of(
      // in der Altersbegrenzung
      Arguments.of(new AgeRestriction(localDateFactory(2010, 1, 1), localDateFactory(2009, 1, 1)),
        playerFactory(2009, 1, 2), FALSE),

      // in der Altersbegrenzung (Taggenau an der Untergrenze)
      Arguments.of(new AgeRestriction(localDateFactory(2010, 1, 1), localDateFactory(2009, 1, 1)),
        playerFactory(2009, 1, 1), FALSE),

      // in der Altersbegrenzung (Taggenau an der Obergrenze)
      Arguments.of(new AgeRestriction(localDateFactory(2010, 1, 1), localDateFactory(2009, 1, 1)),
        playerFactory(2010, 1, 1), FALSE),

      // zu alt (einen Tag)
      Arguments.of(new AgeRestriction(localDateFactory(2010, 1, 1), localDateFactory(2009, 1, 1)),
        playerFactory(2008, 12, 31), TRUE),

      // zu jung (einen Tag)
      Arguments.of(new AgeRestriction(localDateFactory(2010, 1, 1), localDateFactory(2009, 1, 1)),
        playerFactory(2010, 1, 2), TRUE),

      // kein Geburtstag angegeben
      Arguments.of(new AgeRestriction(localDateFactory(2010, 1, 1), localDateFactory(2009, 1, 1)),
        playerFactory(null, null, null), FALSE),

      // keine Untergrenze angegeben
      Arguments.of(new AgeRestriction(localDateFactory(2010, 1, 1), localDateFactory(null, null, null)),
        playerFactory(2009, 1, 1), FALSE));
  }

  private static Player playerFactory(Integer year, Integer month, Integer day) {
    LocalDate dateOfBirth = localDateFactory(year, month, day);
    Person person = new Person("a", "b", dateOfBirth, Gender.UNKNOWN);
    return new Player(person);
  }

  private static LocalDate localDateFactory(Integer year, Integer month, Integer day) {
    if (year != null && month != null && day != null) {
      return LocalDate.of(year, month, day);
    }
    return null;
  }

  @ParameterizedTest
  @MethodSource("addRestrictionAndPlayer")
  void testPlayerRestriction(Restriction restriction, Player player, boolean expectedRestricted) {
    boolean isRestricted = restriction.isRestricted(player);
    assertThat(isRestricted).isEqualTo(expectedRestricted);
  }
}
