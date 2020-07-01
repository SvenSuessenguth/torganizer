package org.cc.torganizer.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ValidateSquadTest {

  private static Validator validator;

  @BeforeAll
  public static void beforeClass() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testValidateSquad_ok() {
    Player p1 = new Player("p1", "p1");
    Player p2 = new Player("p2", "p2");
    Squad squad = new Squad();
    squad.addPlayer(p1);
    squad.addPlayer(p2);

    Set<ConstraintViolation<Squad>> violations = validator.validate(squad);

    // notBlank, min-size=1
    assertThat(violations).hasSize(0);
  }

  @Test
  public void testValidateSquad_notEnoughPlayers() {
    Player p1 = new Player("p1", "p1");
    Squad squad = new Squad();
    squad.addPlayer(p1);

    Set<ConstraintViolation<Squad>> violations = validator.validate(squad);

    // min-size=2
    assertThat(violations).hasSize(1);
  }

  @Test
  public void testValidateSquad_playerNotOk() {
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
