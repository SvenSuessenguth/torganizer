package org.cc.torganizer.core.filter;

import java.util.ArrayList;
import java.util.Collection;
import org.cc.torganizer.core.entities.Opponent;
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
    var result = new ArrayList<Opponent>();

    for (var opponent : opponents) {
      var isRestricted = false;

      // all opponents players must pass
      for (var player : opponent.getPlayers()) {
        for (var restriction : restrictions) {
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
