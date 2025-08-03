package org.cc.torganizer.core.entities;

import org.cc.torganizer.core.exceptions.IllegalDateOfBirthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.Month;
import java.util.stream.Stream;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.cc.torganizer.core.entities.Gender.FEMALE;
import static org.cc.torganizer.core.entities.Gender.MALE;
import static org.cc.torganizer.core.entities.Gender.UNKNOWN;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

  private static final int NOW_YEAR = now().getYear();
  private static final Month NOW_MONTH = now().getMonth();
  private static final int NOW_DAOY_OF_MONTH = now().getDayOfMonth();

  private static Stream<Arguments> testPersonFitsGender() {
    return Stream.of(
      Arguments.of(newPersonWithGender(UNKNOWN), UNKNOWN, true),
      Arguments.of(newPersonWithGender(UNKNOWN), MALE, true),
      Arguments.of(newPersonWithGender(UNKNOWN), FEMALE, true),
      Arguments.of(newPersonWithGender(MALE), MALE, true),
      Arguments.of(newPersonWithGender(MALE), FEMALE, false),
      Arguments.of(newPersonWithGender(null), MALE, true),
      Arguments.of(newPersonWithGender(MALE), null, true),
      Arguments.of(newPersonWithGender(null), null, true)
    );
  }

  @ParameterizedTest
  @MethodSource("testPersonFitsGender")
  void testFitsGender(Person person, Gender gender, boolean expected) {
    var actual = person.fitsGender(gender);

    assertThat(actual).isEqualTo(expected);
  }

  private static Stream<Arguments> testGetPersonAge() {
    return Stream.of(
      Arguments.of(newPersonWitDateOfBirth(of(NOW_YEAR - 1, NOW_MONTH, NOW_DAOY_OF_MONTH)), 1),
      Arguments.of(newPersonWitDateOfBirth(of(NOW_YEAR - 10, NOW_MONTH, NOW_DAOY_OF_MONTH)), 10),
      Arguments.of(newPersonWitDateOfBirth(null), null)
    );
  }

  @ParameterizedTest
  @MethodSource("testGetPersonAge")
  void testGetAge(Person person, Integer expectedAge) {
    var actualAge = person.getAge();

    assertThat(actualAge).isEqualTo(expectedAge);
  }

  @Test
  void testGetAge_0() {
    var person = newPersonWitDateOfBirth(now());

    assertThrows(IllegalDateOfBirthException.class, person::getAge);
  }

  @Test
  void testSetGender_null() {
    var p = new Person();
    p.setGender(null);

    assertThat(p.getGender()).isEqualTo(UNKNOWN);
  }

  private static Person newPersonWithGender(Gender gender) {
    var person = new Person("firstName", "lastName");
    person.setGender(gender);

    return person;
  }

  private static Person newPersonWitDateOfBirth(LocalDate dateOfBirth) {
    var person = new Person("firstName", "lastName");
    person.setDateOfBirth(dateOfBirth);

    return person;
  }
}