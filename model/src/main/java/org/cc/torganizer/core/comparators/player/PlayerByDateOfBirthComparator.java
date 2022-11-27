package org.cc.torganizer.core.comparators.player;

import static org.cc.torganizer.core.comparators.player.PlayerOrderCriteria.BY_DATE_OF_BIRTH;

import jakarta.enterprise.context.RequestScoped;
import java.util.Objects;
import org.cc.torganizer.core.entities.Player;

/**
 * Comparing Players by DateOfBirth.
 */
@RequestScoped
public class PlayerByDateOfBirthComparator implements PlayerComparator {
  @Override
  public int compare(Player player1, Player player2) {
    if (Objects.equals(player1, player2)) {
      return 0;
    }

    if (player1 == null) {
      return -1;
    } else if (player2 == null) {
      return 1;
    }

    var player1DateOfBirth = player1.getPerson().getDateOfBirth();
    var player2DateOfBirth = player2.getPerson().getDateOfBirth();

    if (player1DateOfBirth == null && player2DateOfBirth == null) {
      return 0;
    } else if (player1DateOfBirth == null) {
      return -1;
    } else if (player2DateOfBirth == null) {
      return 1;
    }

    return player1DateOfBirth.compareTo(player2DateOfBirth);
  }

  @Override
  public PlayerOrderCriteria getPlayerOrderCriteria() {
    return BY_DATE_OF_BIRTH;
  }
}
