package org.cc.torganizer.core.entities;

/**
 * Aufzaehlung von verschiedenen Opponent-Typen. Diese werden z.B. als
 * Restriktionen bei Disciplines verwendet.
 */
public enum OpponentType {
  PLAYER(Player.class),
  SQUAD(Squad.class),
  TEAM(Team.class),
  BYE(Bye.class),
  UNKNOWN(Unknown.class);

  private final Class<? extends Opponent> opponentClass;

  /**
   * Konstruktor.
   *
   * @param newOpponentClass Class des Oppoenents
   */
  OpponentType(Class<? extends Opponent> newOpponentClass) {
    this.opponentClass = newOpponentClass;
  }

  @Override
  public String toString() {
    return name();
  }
}
