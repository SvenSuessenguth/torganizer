package org.cc.torganizer.core.entities.aggregates;

import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;

/**
 * Allgemeine Funktionen zur Verdichtung von Daten.
 */
public abstract class AbstractAggregate {

  private int wins;
  private int lose;

  /**
   * Ermittelt das Verhaeltnis zwischen gewonnen und verloren.
   *
   * @return Verhaeltniswert
   */
  public Double getRatio() {
    if (wins + lose == 0) {
      return 0.0;
    }

    return (double) wins / (double) (wins + lose);
  }

  public int getWins() {
    return wins;
  }

  /**
   * Erhoehen der gewonnenen.
   *
   * @param i Inkrement
   */
  void increaseWins(int i) {
    this.wins += i;
  }

  public int getLose() {
    return lose;
  }

  /**
   * Erhoehen der verlorenen.
   *
   * @param i Inkrement
   */
  void increaseLose(int i) {
    this.lose += i;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || this.getClass() != object.getClass()) {
      return false;
    }
    var other = (AbstractAggregate) object;

    return wins == other.wins && lose == other.lose;
  }

  @Override
  public int hashCode() {
    return Integer.valueOf(wins).hashCode() + Integer.valueOf(lose).hashCode();
  }

  /**
   * Aggregieren der Daten zu einem Opponent und einem Match.
   *
   * @param match    Match, dessen Daten aggregiert werden sollen.
   * @param opponent Opponent, dessen Daten aggregiert werden sollen.
   */
  abstract void aggregate(Match match, Opponent opponent);
}
