package org.cc.torganizer.core.entities.aggregates;

import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Result;

/**
 * Aggregieren der Daten zu verlorenen und gewonnenen Punkten.
 */
public class ScoreAggregate
  extends AbstractAggregate {

  public ScoreAggregate() {
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
        increaseWins(result.getHomeScore());
        increaseLose(result.getGuestScore());
      } else {
        increaseWins(result.getGuestScore());
        increaseLose(result.getHomeScore());
      }
    }
  }
}
