package org.cc.torganizer.core.comparators.player;

import static org.cc.torganizer.core.comparators.player.PlayerOrder.BY_LAST_UPDATE;

import java.util.Date;
import jakarta.enterprise.context.RequestScoped;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
public class PlayerByLastUpdateComparator implements PlayerComparator {
  @Override
  public int compare(Player player1, Player player2) {
    Date lastUpdatePlayer1 = player1.getLastUpdate();
    Date lastUpdatePlayer2 = player2.getLastUpdate();

    // NULL-Handling
    if (lastUpdatePlayer1 == null && lastUpdatePlayer2 == null) {
      return 0;
    } else if (lastUpdatePlayer1 == null) {
      return 1;
    } else if (lastUpdatePlayer2 == null) {
      return -1;
    }

    // latest first, so reverse
    return lastUpdatePlayer2.compareTo(lastUpdatePlayer1);
  }

  @Override
  public PlayerOrder getPlayerOrder() {
    return BY_LAST_UPDATE;
  }
}
