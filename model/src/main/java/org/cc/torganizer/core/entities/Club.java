package org.cc.torganizer.core.entities;

/**
 * Verein.
 */
public class Club extends Entity {

  /**
   * zu verwendender Name, wenn ein Opponent keinem club angehoert.
   */
  public static final String NULL_CLUB_NAME = "-";

  private String name;

  /**
   * Default.
   */
  public Club() {
    // gem. Bean-Spec.
  }

  /**
   * Konstruktor.
   *
   * @param name Name des Clubs
   */
  public Club(final String name) {
    this.name = name;
  }

  public final String getName() {
    return name;
  }

  public final void setName(final String name) {
    this.name = name;
  }
}
