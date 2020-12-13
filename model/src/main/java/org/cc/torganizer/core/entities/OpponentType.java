package org.cc.torganizer.core.entities;

/**
 * Aufzaehlung von verschiedenen Opponent-Typen. Diese werden z.B. als
 * Restriktionen bei Disciplines verwendet.
 */
public enum OpponentType {
  PLAYER(Player.class),
  SQUAD(Squad.class),
  TEAM(Team.class),
  Bye(Bye.class),
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

  /**
   * Gibt den OpponentType zu einer uebergebenen Class zurueck.
   *
   * @param opponentClass Class eines Opponents
   * @return OpponentType
   */
  public static OpponentType fromClass(Class<? extends Opponent> opponentClass) {
    for (OpponentType opponentType : OpponentType.values()) {
      if (opponentType.getOpponentClass().equals(opponentClass)) {
        return opponentType;
      }
    }

    return null;
  }

  @Override
  public String toString() {
    return name();
  }

  public Class<? extends Opponent> getOpponentClass() {
    return opponentClass;
  }
}
