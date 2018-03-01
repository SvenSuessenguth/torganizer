package org.cc.torganizer.core.entities;

/**
 * Spielfeld.
 */
public class Court extends Entity {

  private Match match;

  private int nr;

  /**
   * Default.
   */
  public Court() {
    // gem. Bean-Spec.
  }

  public Match getMatch() {
    return match;
  }

  public void setMatch(Match newMatch) {
    this.match = newMatch;
  }

  public int getNr() {
    return nr;
  }

  public void setNr(int newNr) {
    this.nr = newNr;
  }
}
