package org.cc.torganizer.core.entities;

import lombok.Getter;

/**
 * Type for all restrictions.
 */
public abstract class Restriction extends Entity {

  /**
   * Gibt an, ob es für den übergebenen Opponent eine Restriktion
   * gibt, die eine Teilnahme verbietet.
   *
   * @param opponent Opponent, der geprüft werden soll.
   * @return <code>true</code>, wenn die Restriktionsregel erfüllt ist,
   * sonst <code>false</code>
   */
  public abstract boolean isRestricted(Opponent opponent);

  public abstract Discriminator getDiscriminator();

  /**
   * Discriminator with factory-methods for restrictions. This discriminator is
   * not used for JPA.
   */
  @Getter
  public enum Discriminator {
    AGE_RESTRICTION("A") {
      @Override
      public Restriction create() {
        return new AgeRestriction();
      }
    },
    GENDER_RESTRICTION("G") {
      @Override
      public Restriction create() {
        return new GenderRestriction();
      }
    },
    OPPONENT_TYPE_RESTRICTION("O") {
      @Override
      public Restriction create() {
        return new OpponentTypeRestriction();
      }
    };

    private final String id;

    Discriminator(String discriminator) {
      this.id = discriminator;
    }

    public abstract Restriction create();
  }
}
