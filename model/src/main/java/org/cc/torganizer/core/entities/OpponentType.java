package org.cc.torganizer.core.entities;

/**
 * Aufzaehlung von verschiedenen Opponent-Typen. Diese werden z.B. als
 * Restriktionen bei Disciplines verwendet.
 */
public enum OpponentType {
  PLAYER,
  SQUAD,
  TEAM,
  BYE,
  UNKNOWN;

  @Override
  public String toString() {
    return name();
  }
}
