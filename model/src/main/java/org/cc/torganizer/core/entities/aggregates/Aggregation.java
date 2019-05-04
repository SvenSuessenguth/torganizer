package org.cc.torganizer.core.entities.aggregates;

import org.cc.torganizer.core.entities.Opponent;

/**
 * Zusammenfassung aller aggregierten Daten eines Opponents ueber alle Matches,
 * Results (entsprechen Saetzen) und den Scores.
 */
public class Aggregation {
  private Opponent opponent;
  private MatchAggregate ma;
  private ResultAggregate ra;
  private ScoreAggregate sa;

  public Opponent getOpponent() {
    return opponent;
  }

  public void setOpponent(Opponent newOpponent) {
    this.opponent = newOpponent;
  }

  public MatchAggregate getMa() {
    return ma;
  }

  public void setMa(MatchAggregate newMa) {
    this.ma = newMa;
  }

  public ResultAggregate getRa() {
    return ra;
  }

  public void setRa(ResultAggregate newRa) {
    this.ra = newRa;
  }

  public ScoreAggregate getSa() {
    return sa;
  }

  public void setSa(ScoreAggregate newSa) {
    this.sa = newSa;
  }
}
