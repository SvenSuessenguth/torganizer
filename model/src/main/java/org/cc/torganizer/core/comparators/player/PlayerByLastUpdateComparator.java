package org.cc.torganizer.core.comparators.player;

import static org.cc.torganizer.core.comparators.player.PlayerOrderCriteria.BY_LAST_UPDATE;

import jakarta.enterprise.context.RequestScoped;
import org.cc.torganizer.core.entities.Player;

/**
 * Comparing Players by LastUpdate.
 */
@RequestScoped
public class PlayerByLastUpdateComparator implements PlayerComparator {
  @Override
  public int compare(Player player1, Player player2) {
    var lastUpdatePlayer1 = player1.getLastUpdate();
    var lastUpdatePlayer2 = player2.getLastUpdate();

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
  public PlayerOrderCriteria getPlayerOrderCriteria() {
    return BY_LAST_UPDATE;
  }
}
