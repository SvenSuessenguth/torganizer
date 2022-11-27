package org.cc.torganizer.core.entities;

/**
 * Gender.
 */
public enum Gender {

  MALE("M"),
  FEMALE("F"),
  UNKNOWN("U");

  private final String id;

  Gender(String id) {
    this.id = id;
  }

  /**
   * Nullsafe converting a given id to a Gender-Object.
   */
  public static Gender byId(String id) {
    for (var gender : values()) {
      if (gender.id.equals(id)) {
        return gender;
      }
    }

    return null;
  }

  public String getId() {
    return id;
  }
}
