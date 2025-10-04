package org.cc.torganizer.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

/**
 * Restriktion bezueglich des Geschlechtes.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class GenderRestriction extends Restriction {

  private static final Discriminator DISCRIMINATOR = Discriminator.GENDER_RESTRICTION;

  private Gender gender = UNKNOWN;

  @Override
  public boolean isRestricted(Opponent opponent) {

    var isRestricted = false;

    for (var player : opponent.getPlayers()) {
      // gender does not fit
      if (player.hasGender() && isGenderRestricted(player)) {
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
   * @return <code>true</code>, wenn das Gender des Players nicht mit der Vorgabe uebereinstimmt
   * und die Vorgabe nicht UNKNOWN ist, sonst <code>false</code>
   */
  protected boolean isGenderRestricted(Player player) {
    var playersGender = player.getPerson().getGender();

    // either the gender of the restriction or the gender of the player is unknown
    if (UNKNOWN.equals(gender) || UNKNOWN.equals(playersGender) || playersGender == null) {
      return false;
    }

    // no gender is UNKNOWN, so the genders must be equal
    return !this.gender.equals(playersGender);
  }

  @Override
  public String toString() {
    return "GenderRestriction with gender='" + gender + "'";
  }

  @Override
  public Discriminator getDiscriminator() {
    return GenderRestriction.DISCRIMINATOR;
  }
}
