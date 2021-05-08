package org.cc.torganizer.core.filter;

import java.util.ArrayList;
import java.util.Collection;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Restriction;

/**
 * Filtering Opponents.
 */
public class OpponentFilter {

  /**
   * Returns the passing opponents.
   */
  public Collection<Opponent> pass(Collection<Opponent> opponents,
                                   Collection<Restriction> restrictions) {
    Collection<Opponent> result = new ArrayList<>();

    for (Opponent opponent : opponents) {
      var isRestricted = false;

      // all opponents players must pass
      for (Player player : opponent.getPlayers()) {
        for (Restriction restriction : restrictions) {
          isRestricted = isRestricted || restriction.isRestricted(player);
        }
      }

      if (!isRestricted) {
        result.add(opponent);
      }
    }

    return result;
  }
}
