package org.cc.torganizer.validation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class ValidateSquadTest {

  private static Validator validator;

  @BeforeAll
  public static void beforeClass() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void testValidateSquad_ok() {
    Player p1 = new Player("p1", "p1");
    Player p2 = new Player("p2", "p2");
    Squad squad = new Squad();
    squad.addPlayer(p1);
    squad.addPlayer(p2);

    Set<ConstraintViolation<Squad>> violations = validator.validate(squad);

    // notBlank, min-size=1
    assertThat(violations).isEmpty();
  }

  @Test
  void testValidateSquad_notEnoughPlayers() {
    Player p1 = new Player("p1", "p1");
    Squad squad = new Squad();
    squad.addPlayer(p1);

    Set<ConstraintViolation<Squad>> violations = validator.validate(squad);

    // min-size=2
    assertThat(violations).hasSize(1);
  }

  @Test
  void testValidateSquad_playerNotOk() {
    Player p1 = new Player("p1", "p1");
    Player p2 = new Player("p2", null);
    Squad squad = new Squad();
    squad.addPlayer(p1);
    squad.addPlayer(p2);

    Set<ConstraintViolation<Squad>> violations = validator.validate(squad);

    // lastName must not be null or empty
    assertThat(violations).hasSize(2);
  }
}
