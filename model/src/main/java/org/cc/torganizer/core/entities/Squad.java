package org.cc.torganizer.core.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * Ein Squad ist eine Zusammenfassung mehrerer Players. Dies kann z.B. im
 * Badminton oder Tennis ein Doppel sein.
 */
public class Squad extends Opponent {

  /** Ersatz f\u00fcr null-Values. */
  public static final Squad NONE = new Squad();

  private Set<Player> players = new HashSet<>();

  /**
   * Default.
   */
  public Squad() {
    // gem. Bean-Spec.
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  /** {@inheritDoc} */
  @Override
  public Set<Player> getPlayers() {
    return players;
  }

  public void setPlayers(Set<Player> newPlayers) {
    this.players = newPlayers;
  }
}
