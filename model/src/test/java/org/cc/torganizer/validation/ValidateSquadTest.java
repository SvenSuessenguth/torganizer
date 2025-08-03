package org.cc.torganizer.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ValidateSquadTest {

  private static Validator validator;

  @BeforeAll
  static void beforeClass() {
    var factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidateSquad_ok() {
    var p1 = new Player("p1", "p1");
    var p2 = new Player("p2", "p2");
    var squad = new Squad();
    squad.addPlayer(p1);
    squad.addPlayer(p2);

    var violations = validator.validate(squad);

    // notBlank, min-size=1
    assertThat(violations).isEmpty();
  }

  @Test
  void testValidateSquad_notEnoughPlayers() {
    var p1 = new Player("p1", "p1");
    var squad = new Squad();
    squad.addPlayer(p1);

    var violations = validator.validate(squad);

    // min-size=2
    assertThat(violations).hasSize(1);
  }

  @Test
  void testValidateSquad_playerNotOk() {
    var p1 = new Player("p1", "p1");
    var p2 = new Player("p2", null);
    var squad = new Squad();
    squad.addPlayer(p1);
    squad.addPlayer(p2);

    var violations = validator.validate(squad);

    // lastName must not be null or empty
    assertThat(violations).hasSize(2);
  }
}
