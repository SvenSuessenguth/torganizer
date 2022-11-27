package org.cc.torganizer.core.entities.aggregates;

import java.util.Objects;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;

/**
 * Aggregieren der gewonnenen und verlorenen Saetze (Results).
 */
public class ResultAggregate extends AbstractAggregate {

  @Override
  public void aggregate(Match match, Opponent opponent) {
    if (!match.getOpponents().contains(opponent)) {
      return;
    }

    var isHome = Objects.equals(match.getHome(), opponent);
    for (var result : match.getResults()) {
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
