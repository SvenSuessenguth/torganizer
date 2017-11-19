package org.cc.torganizer.core.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Restriktion bezueglich des Geschlechtes.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "GenderRestriction")
@XmlAccessorType(XmlAccessType.NONE)
public class GenderRestriction
  extends Restriction {
  
  @XmlAttribute
  private Gender validGender = Gender.UNKNOWN;

  /**
   * Default.
   */
  public GenderRestriction() {
    // gem. Bean-Spec.
  }

  /** {@inheritDoc} */
  @Override
  public boolean isRestricted(Opponent opponent) {

    boolean isRestricted = false;

    for (Player player : opponent.getPlayers()) {
      // gender stimmt nicht ueberein
      if (isGenderSet(player) && isGenderRestricted(player)) {
        isRestricted = true;
      }
    }

    return isRestricted;
  }

  /**
   * Prueft, ob die Gender ungleich sind und das Gender der GenderRestriction
   * nicht null oder UNKNOWN ist.
   * 
   * @param player Player
   * @return <code>true</code>, wenn das Gender des Players nicht mit der
   *         Vorgabe uebereinstimmt und die Vorgabe nicht UNKNOWN ist, sonst
   *         <code>false</code>
   */
  protected boolean isGenderRestricted(Player player) {
    Gender gender = player.getPerson().getGender();

    return !Gender.UNKNOWN.equals(validGender) && !validGender.equals(gender);
  }

  /**
   * Prueft, ob das Gender des Players bekannt ist (also nicht UNKNOWN).
   * 
   * @param player Player, dessen Gender geprueft werden soll.
   * @return <code>true</code>, wenn das Gender der Person nicht
   *         <code>null</code> und nicht unbekannt ist, sonst <code>false</code>
   */
  protected boolean isGenderSet(Player player) {
    boolean isGenderSet = true;

    Gender gender = player.getPerson().getGender();
    if (gender == null || Gender.UNKNOWN.equals(gender)) {
      isGenderSet = false;
    }

    return isGenderSet;
  }

  public Gender getValidGender() {
    return validGender;
  }

  public void setValidGender(Gender newValidGender) {
    this.validGender = newValidGender;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "GenderRestriction with validGender='" + validGender + "'";
  }
}
