package org.cc.torganizer.core.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;

import java.time.LocalDate;

/**
 * Restriktion auf Teilnahme an einem Modus wegen der Altersbeschraenkung. Fehlt
 * die obere oder untere Grenze, ist das Intervall in die entsprechende Richtung
 * offen.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AgeRestriction extends Restriction {

  /**
   * Discriminator for Database.
   */
  private static final Discriminator DISCRIMINATOR = AGE_RESTRICTION;

  /**
   * liegt zeitlich NACH dem minDateOfBirth (hat höheren Jahres-, Monats-
   * oder Tageswert).
   */
  private LocalDate maxDateOfBirth;

  /**
   * liegt zeitlich VOR dem maxDateOfBirth (hat niedrigeren Jahres-, Monats-
   * oder Tageswert).
   */
  private LocalDate minDateOfBirth;

  /**
   * Default.
   */
  public AgeRestriction() {
    // gem. Bean-Spec.
  }

  public AgeRestriction(int minAge, int maxAge) {
    this.setMinDateOfBirth(LocalDate.now().minusYears(maxAge));
    this.setMaxDateOfBirth(LocalDate.now().minusYears(minAge));
  }

  /**
   * convinience-constructor.
   *
   * @param newMaxDateOfBirth lower bound of age
   * @param newMinDateOfBirth upper bound of age
   */
  public AgeRestriction(LocalDate newMaxDateOfBirth,
                        LocalDate newMinDateOfBirth) {
    this.maxDateOfBirth = newMaxDateOfBirth;
    this.minDateOfBirth = newMinDateOfBirth;
  }

  @Override
  public boolean isRestricted(Opponent opponent) {
    // wenn es eine Ober- bzw. eine Untergrenze fuer das Geburtsdatum
    // dann muss diese auch eingehalten werden

    // restricted is true, if one of the opponents players is restricted
    var restricted = false;

    for (Player player : opponent.getPlayers()) {
      LocalDate dateOfBirth = player.getPerson().getDateOfBirth();

      // no dateOfBirth is not restricted
      if (dateOfBirth == null) {
        return false;
      }

      if (maxDateOfBirth != null && maxDateOfBirth.isBefore(dateOfBirth)) {
        restricted = true;
      }
      if (minDateOfBirth != null && minDateOfBirth.isAfter(dateOfBirth)) {
        restricted = true;
      }
    }
    return restricted;
  }

  @Override
  public String toString() {
    return "AgeRestriction with maxDateOfBirth='"
        + maxDateOfBirth
        + "' and minDateOfBirth='"
        + minDateOfBirth
        + "'";
  }

  @Override
  public Discriminator getDiscriminator() {
    return AgeRestriction.DISCRIMINATOR;
  }
}
