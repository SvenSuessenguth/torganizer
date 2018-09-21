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
   * @param pName Name des Clubs
   */
  public Club(String pName) {
    this.name = pName;
  }

  public String getName() {
    return name;
  }

  public void setName(String newName) {
    this.name = newName;
  }
}
