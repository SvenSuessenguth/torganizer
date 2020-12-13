package org.cc.torganizer.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ValidatePlayerTest {

  private static Validator validator;

  @BeforeAll
  static void beforeClass() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidatePlayer_firstNameTooShort() {
    String tooShortFirstName = "";
    Person p = new Person(tooShortFirstName, "ok");
    Player player = new Player(p);

    Set<ConstraintViolation<Player>> violations = validator.validate(player);

    // notBlank, min-size=1
    assertThat(violations).hasSize(2);
  }

  @Test
  void testValidatePlayer_firstNameTooLong() {
    String tooShortFirstName = "012345678901234567890123456789";
    Person p = new Person(tooShortFirstName, "ok");
    Player player = new Player(p);

    Set<ConstraintViolation<Player>> violations = validator.validate(player);

    // notBlank, min-size=1
    assertThat(violations).hasSize(1);
  }
}
