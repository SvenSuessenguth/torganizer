package org.cc.torganizer.core.entities;

/**
 * Spielfeld.
 */
public class Court extends Entity {

  private Match match;

  private int nr;

  public final Match getMatch() {
    return match;
  }

  public final void setMatch(final Match newMatch) {
    this.match = newMatch;
  }

  public final int getNr() {
    return nr;
  }

  public final void setNr(final int newNr) {
    this.nr = newNr;
  }
}
