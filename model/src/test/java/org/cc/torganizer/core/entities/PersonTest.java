package org.cc.torganizer.core.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PersonTest {

  private static Stream<Arguments> addMatch() {
    return Stream.of(
        Arguments.of(personFactory(UNKNOWN), UNKNOWN, true),
        Arguments.of(personFactory(UNKNOWN), MALE, true),
        Arguments.of(personFactory(UNKNOWN), FEMALE, true),
        Arguments.of(personFactory(MALE), MALE, true),
        Arguments.of(personFactory(MALE), FEMALE, false),
        Arguments.of(personFactory(null), MALE, true),
        Arguments.of(personFactory(MALE), null, true)
    );
  }

  @ParameterizedTest
  @MethodSource("addMatch")
  void test(Person person, Gender gender, boolean expected) {
    boolean actual = person.fitsGender(gender);

    assertThat(actual).isEqualTo(expected);
  }

  private static Person personFactory(Gender gender) {
    Person person = new Person("firstName", "lastName");
    person.setGender(gender);

    return person;
  }

}