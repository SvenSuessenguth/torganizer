package org.cc.torganizer.core.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Der Status zeigt an, ob ein Spieler spielbereit ist, oder dem Turnier nicht
 * mehr zu Verf\u00fcgung steht. Der Status sagt nicht, ob der Spieler spielt
 * oder spielfrei hat.
 */
@XmlRootElement(name = "Status")
@XmlAccessorType(XmlAccessType.FIELD)
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

  private int value;

  /**
   * Konstruktor.
   * 
   * @param inValue ID
   */
  Status(int inValue) {
    this.value = inValue;
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
   * Gibt den Status zu einer ID zurueck.
   *
   * @param value ID
   * @return Status
   */
  public static Status fromInt(int value) {
    switch (value) {
    case 1:
      return ACTIVE;
    case 0:
      return INACTIVE;
    default:
      return INACTIVE;
    }
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    switch (value) {
    case 1:
      return "ACTIVE";
    case 0:
      return "INACTIVE";
    default:
      return "INACTIVE";
    }
  }
}
