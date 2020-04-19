package org.cc.torganizer.core.entities;

import java.util.Objects;

/**
 * Club.
 */
public class Club extends Entity {

  private String name;

  public Club() {
    // default
  }

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
