package org.cc.torganizer.core.entities;

/**
 * Der Status zeigt an, ob ein Spieler spielbereit ist, oder dem Turnier nicht
 * mehr zu Verf\u00fcgung steht. Der Status sagt nicht, ob der Spieler spielt
 * oder spielfrei hat.
 */
public enum Status {
  /**
   * Aktiv.
   */
  ACTIVE(1),

  /**
   * Inaktiv und aus dem Turnier ausgeschieden. Dies gilt nur fuer Ausscheiden
   * durch Verletzung, nicht antreten oder aehnlichen Faellen. Inactive bezieht
   * sich also auf alle Disciplines.
   */
  INACTIVE(0);

  private final int value;

  /**
   * Konstruktor.
   *
   * @param inValue ID
   */
  Status(int inValue) {
    this.value = inValue;
  }

  /**
   * Gibt den Status zu einer ID zurueck.
   *
   * @param value ID
   * @return Status
   */
  public static Status fromInt(int value) {
    for (Status status : values()) {
      if (status.value == value) {
        return status;
      }
    }

    return null;
  }

  /**
   * Gibt die ID zurueck.
   *
   * @return ID
   */
  public int toInt() {
    return value;
  }
}
