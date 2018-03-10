package org.cc.torganizer.core.entities;

/**
 * Abstrakte Oberklasse zu allen Restriktionen.
 */
public abstract class Restriction extends Entity {

  public static final String AGE_RESTRICTION = "ageRestriction";
  public static final String GENDER_RESTRICTION = "genderRestriction";
  public static final String OPPONENT_TYPE_RESTRICTION = "opponentTypeRestriction";

  public enum Type {
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

    private final String discriminator;

    Type(String discriminator) {
      this.discriminator = discriminator;
    }

    public abstract Restriction create();

    public String getDiscriminator() {
      return discriminator;
    }

    public static Type byDiscriminator(String discriminator) {
      for (Type type : values()) {
        if (type.discriminator.equals(discriminator)) {
          return type;
        }
      }

      throw new IllegalArgumentException("no Restriction.Type found for discriminator '" + discriminator + "'");
    }
  }

  /**
   * Gibt an, ob es f\u00fcr den \u00fcbergebenen Opponent eine Restriktion
   * gibt, die eine Teilnahme verbietet.
   *
   * @param opponent Opponent, der gepr\u00fcft werden soll.
   * @return <code>true</code>, wenn die Restriktionsregel erf\u00fcllt ist,
   * sonst <code>false</code>
   */
  public abstract boolean isRestricted(Opponent opponent);
}
