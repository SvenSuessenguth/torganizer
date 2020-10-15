package org.cc.torganizer.core.entities;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.Gender.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;
import org.cc.torganizer.core.exceptions.IllegalDateOfBirthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PersonTest {

  private static final int nowYear = now().getYear();
  private static final Month nowMonth = now().getMonth();
  private static final int nowDayOfMonth = now().getDayOfMonth();

  private static Stream<Arguments> testFitsGender() {
    return Stream.of(
        Arguments.of(newPersonWithGender(UNKNOWN), UNKNOWN, true),
        Arguments.of(newPersonWithGender(UNKNOWN), MALE, true),
        Arguments.of(newPersonWithGender(UNKNOWN), FEMALE, true),
        Arguments.of(newPersonWithGender(MALE), MALE, true),
        Arguments.of(newPersonWithGender(MALE), FEMALE, false),
        Arguments.of(newPersonWithGender(null), MALE, true),
        Arguments.of(newPersonWithGender(MALE), null, true)
    );
  }

  private static Stream<Arguments> testGetAge() {
    return Stream.of(
        Arguments.of(newPersonWitDateOfBirth(of(nowYear - 1, nowMonth, nowDayOfMonth)), 1),
        Arguments.of(newPersonWitDateOfBirth(of(nowYear - 10, nowMonth, nowDayOfMonth)), 10),
        Arguments.of(newPersonWitDateOfBirth(null), null)
    );
  }

  private static Person newPersonWithGender(Gender gender) {
    Person person = new Person("firstName", "lastName");
    person.setGender(gender);

    return person;
  }

  private static Person newPersonWitDateOfBirth(LocalDate dateOfBirth) {
    Person person = new Person("firstName", "lastName");
    person.setDateOfBirth(dateOfBirth);

    return person;
  }

  @ParameterizedTest
  @MethodSource("testFitsGender")
  void testFitsGender(Person person, Gender gender, boolean expected) {
    boolean actual = person.fitsGender(gender);

    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("testGetAge")
  void testGetAge(Person person, Integer expectedAge) {
    Integer actualAge = person.getAge();

    assertThat(actualAge).isEqualTo(expectedAge);
  }

  @Test
  void testGetAge_0() {
    Person person = newPersonWitDateOfBirth(now());

    assertThrows(IllegalDateOfBirthException.class, () -> person.getAge());
  }
}