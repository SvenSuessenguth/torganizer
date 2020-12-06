package org.cc.torganizer.core.comparators.player;

import static org.cc.torganizer.core.comparators.player.PlayerOrder.BY_CLUB;

import jakarta.enterprise.context.RequestScoped;
import org.cc.torganizer.core.comparators.ClubComparator;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
public class PlayerByClubComparator implements PlayerComparator {
  @Override
  public int compare(Player player1, Player player2) {
    Club clubPlayer1 = player1.getClub();
    Club clubPlayer2 = player2.getClub();

    // NULL-Handling
    if (clubPlayer1 == null && clubPlayer2 == null) {
      return 0;
    } else if (clubPlayer1 == null) {
      return 1;
    } else if (clubPlayer2 == null) {
      return -1;
    }

    // both players having clubs

    return new ClubComparator().compare(clubPlayer1, clubPlayer2);
  }

  @Override
  public PlayerOrder getPlayerOrder() {
    return BY_CLUB;
  }
}
