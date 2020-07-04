package org.cc.torganizer.core.builder;

import java.time.LocalDate;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Player;

public class PlayerBuilder {

  private Player player;

  /**
   * Male player with age 20.
   */
  public PlayerBuilder standard() {
    player = new Player("firstName", "lastName");
    withAge(20);
    withGender(Gender.MALE);

    return this;
  }

  public PlayerBuilder withAge(int age) {
    if (player == null) {
      standard();
    }

    player.getPerson().setDateOfBirth(LocalDate.now().minusYears(age));

    return this;
  }

  public PlayerBuilder withGender(Gender gender) {
    if (player == null) {
      standard();
    }

    player.getPerson().setGender(gender);

    return this;
  }

  public Player get() {
    if (player == null) {
      standard();
    }

    return player;
  }
}
