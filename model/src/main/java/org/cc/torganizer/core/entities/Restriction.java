package org.cc.torganizer.core.entities;

/**
 * Type for all restrictions.
 */
public abstract class Restriction extends Entity {

  /**
   * Gibt an, ob es f\u00fcr den \u00fcbergebenen Opponent eine Restriktion
   * gibt, die eine Teilnahme verbietet.
   *
   * @param opponent Opponent, der gepr\u00fcft werden soll.
   * @return <code>true</code>, wenn die Restriktionsregel erf\u00fcllt ist,
   *     sonst <code>false</code>
   */
  public abstract boolean isRestricted(Opponent opponent);

  public abstract Discriminator getDiscriminator();

  /**
   * Discriminator with factory-methods for restrictions. This discriminator is
   * not used for JPA.
   */
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

    public String getId() {
      return id;
    }
  }
}
