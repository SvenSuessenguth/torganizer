package org.cc.torganizer.core.comparators.player;

import org.cc.torganizer.core.entities.Player;

import javax.enterprise.context.RequestScoped;
import java.time.LocalDate;
import java.util.Objects;

import static org.cc.torganizer.core.comparators.player.PlayerOrder.BY_DATE_OF_BIRTH;

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

    LocalDate player1DateOfBirth = player1.getPerson().getDateOfBirth();
    LocalDate player2DateOfBirth = player2.getPerson().getDateOfBirth();

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
  public PlayerOrder getPlayerOrder() {
    return BY_DATE_OF_BIRTH;
  }
}
