package org.cc.torganizer.core.entities.aggregates;

import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Result;

/**
 * Aggregieren der gewonnenen und verlorenen Saetze (Results).
 * 
 * @author svens
 */
public class ResultAggregate
  extends AbstractAggregate {

  /**
   * Default.
   */
  public ResultAggregate() {
    // gem. Bean-Spec.
  }

  @Override
  public void aggregate(Match match, Opponent opponent) {
    if (!match.getOpponents().contains(opponent)) {
      return;
    }

    boolean isHome = match.getHome().equals(opponent);
    for (Result result : match.getResults()) {
      if (isHome) {
        if (result.getHomeScore() > result.getGuestScore()) {
          increaseWins(1);
        } else {
          increaseLose(1);
        }
      } else {
        if (result.getHomeScore() > result.getGuestScore()) {
          increaseLose(1);
        } else {
          increaseWins(1);
        }
      }
    }
  }
}
