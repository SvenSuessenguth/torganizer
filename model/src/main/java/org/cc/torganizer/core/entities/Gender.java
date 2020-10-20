package org.cc.torganizer.core.entities;

/**
 * Gender.
 */
public enum Gender {

  MALE,
  FEMALE,
  UNKNOWN;


  /**
   * Nullsafe converting a given name to a Gender-Object.
   */
  public static Gender byName(String name) {
    for (Gender gender : values()) {
      if (gender.name().equals(name)) {
        return gender;
      }
    }

    return null;
  }
}
