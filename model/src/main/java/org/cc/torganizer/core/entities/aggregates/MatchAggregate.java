package org.cc.torganizer.core.entities.aggregates;

import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Unknown;

/**
 * Aggregieren der Siege und Niederlagen.
 */
public class MatchAggregate
  extends AbstractAggregate {

  public MatchAggregate() {
    // gem. Bean-Spec.
  }

  @Override
  public void aggregate(Match match, Opponent opponent) {
    if (!match.getOpponents().contains(opponent) || match.getWinner() instanceof Unknown) {
      return;
    }

    if (opponent.equals(match.getWinner())) {
      increaseWins(1);
    }else if (opponent.equals(match.getLoser())) {
      increaseLose(1);
    }
  }
}
