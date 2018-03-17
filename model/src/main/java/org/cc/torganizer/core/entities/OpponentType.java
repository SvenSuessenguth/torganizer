package org.cc.torganizer.core.entities;

/**
 * Aufzaehlung von verschiedenen Opponent-Typen. Diese werden z.B. als
 * Restriktionen bei Disciplines verwendet.
 */
public enum OpponentType {
  /** Ein Player. */
  PLAYER(0, Player.class),

  /** Mehrere Player. */
  SQUAD(1, Squad.class),

  /** Players und/oder Squads. */
  TEAM(2, Team.class);

  private final int value;

  private final Class<? extends Opponent> opponentClass;

  /**
   * Konstruktor.
   * 
   * @param newValue ID
   * @param newOpponentClass Class des Oppoenents
   */
  OpponentType(int newValue, Class<? extends Opponent> newOpponentClass) {
    this.value = newValue;
    this.opponentClass = newOpponentClass;
  }

  /**
   * Gibt die ID zurueck.
   *
   * @return ID
   */
  public int toInt() {
    return value;
  }

  /**
   * Gibt den OpponentType mit der angegebenen ID zurueck.
   *
   * @param value ID
   * @return OpponentType mit der ID oder <code>null</code>
   */
  public static OpponentType fromInt(int value) {
    for (OpponentType opponentType : OpponentType.values()) {
      if (opponentType.value == value) {
        return opponentType;
      }
    }

    return null;
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

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return name();
  }

  public Class<? extends Opponent> getOpponentClass() {
    return opponentClass;
  }
}
