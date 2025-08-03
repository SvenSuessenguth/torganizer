package org.cc.torganizer.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.cc.torganizer.core.entities.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;


class ValidatePersonTest {

  private static Validator validator;

  static Stream<Arguments> personProvider() {
    return Stream.of(
      arguments(new Person("Firstname", "Lastname"), 0),
      arguments(new Person("", "ok"), 2),
      arguments(new Person("    ", "ok"), 1),
      arguments(new Person(null, "ok"), 2),
      arguments(new Person("012345678901234567890", "ok"), 1)
    );
  }

  @BeforeAll
  static void beforeClass() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @ParameterizedTest
  @MethodSource("personProvider")
  void test(Person person, int expectedMessageCount) {
    var violations = validator.validate(person);
    assertThat(violations).hasSize(expectedMessageCount);
  }
}
