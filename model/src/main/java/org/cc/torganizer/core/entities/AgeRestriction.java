package org.cc.torganizer.core.entities;

import java.time.LocalDate;

import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;

/**
 * Restriktion auf Teilnahme an einem Modus wegen der Altersbeschraenkung. Fehlt
 * die obere oder untere Grenze, ist das Intervall in die entsprechende Richtung
 * offen.
 */
public class AgeRestriction extends Restriction {

  /**
   * Discriminator for Database.
   */
  private static final Discriminator DISCRIMINATOR = AGE_RESTRICTION;

  /**
   * liegt zeitlich NACH dem minDateOfBirth (hat h\u00f6heren Jahres-, Monats-
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

  /**
   * convinience-constructor.
   * @param newMaxDateOfBirth lower bound of age
   * @param  newMinDateOfBirth upper bound of age
   */
  public AgeRestriction(final LocalDate newMaxDateOfBirth,
                        final LocalDate newMinDateOfBirth) {
    this.maxDateOfBirth = newMaxDateOfBirth;
    this.minDateOfBirth = newMinDateOfBirth;
  }

  public final LocalDate getMaxDateOfBirth() {
    return maxDateOfBirth;
  }

  public final void setMaxDateOfBirth(final LocalDate newMaxDateOfBirth) {
    this.maxDateOfBirth = newMaxDateOfBirth;
  }

  public final LocalDate getMinDateOfBirth() {
    return minDateOfBirth;
  }

  public final void setMinDateOfBirth(final LocalDate newMinDateOfBirth) {
    this.minDateOfBirth = newMinDateOfBirth;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean isRestricted(final Opponent opponent) {
    // wenn es eine Ober- bzw. eine Untergrenze fuer das Geburtsdatum
    // dann muss diese auch eingehalten werden

    // restricted is true, if one of the opponents players is restricted
    boolean restricted = false;

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
  public final String toString() {
    return "AgeRestriction with maxDateOfBirth='"
      + maxDateOfBirth
      + "' and minDateOfBirth='"
      + minDateOfBirth
      + "'";
  }

  @Override
  public final Discriminator getDiscriminator() {
    return AgeRestriction.DISCRIMINATOR;
  }
}
