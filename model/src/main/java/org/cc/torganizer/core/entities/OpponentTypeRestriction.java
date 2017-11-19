package org.cc.torganizer.core.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Restriktion bezueglich des Typs des spielberechtigten Opponents. Z.B. darf
 * nur Einzel- oder nur Doppel gespielt werden.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "OpponentTypeRestriction")
@XmlAccessorType(XmlAccessType.NONE)
public class OpponentTypeRestriction
  extends Restriction {

  @XmlAttribute
  private OpponentType validOpponentType = OpponentType.PLAYER;

  /**
   * Default.
   */
  public OpponentTypeRestriction() {
    // gem. Bean-Spec.
  }

  /** {@inheritDoc} */
  @Override
  @SuppressWarnings("unchecked")
  public boolean isRestricted(Opponent opponent) {

    Class<? extends Opponent> validOpponentClass = validOpponentType.getOpponentClass();
    Class<Opponent> opponentClass = (Class<Opponent>) opponent.getClass();
    boolean isRestricted = !validOpponentClass.equals(opponentClass);

    return isRestricted;
  }

  public OpponentType getValidOpponentType() {
    return validOpponentType;
  }

  public void setValidOpponentType(OpponentType newValidOpponentType) {
    this.validOpponentType = newValidOpponentType;
  }
  
  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "OpponentTypeRestriction with validOpponentType: " + validOpponentType;
  }
}
