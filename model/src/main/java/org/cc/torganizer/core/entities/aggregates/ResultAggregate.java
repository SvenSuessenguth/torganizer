package org.cc.torganizer.core.entities.aggregates;

import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Result;

import java.util.Objects;

/**
 * Aggregieren der gewonnenen und verlorenen Saetze (Results).
 */
public class ResultAggregate extends AbstractAggregate {

  @Override
  public void aggregate(Match match, Opponent opponent) {
    if (!match.getOpponents().contains(opponent)) {
      return;
    }

    boolean isHome = Objects.equals(match.getHome(), opponent);
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
