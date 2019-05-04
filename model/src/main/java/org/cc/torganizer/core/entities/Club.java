package org.cc.torganizer.core.entities;

/**
 * Verein.
 */
public class Club extends Entity {

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
  public Club(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
