package org.cc.torganizer.core.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Ein Squad ist eine Zusammenfassung mehrerer Players. Dies kann z.B. im
 * Badminton oder Tennis ein Doppel sein.
 */
public class Squad extends Opponent {

  private Set<Player> players = new HashSet<>();

  /**
   * Default.
   */
  public Squad() {
    // gem. Bean-Spec.
  }

  public Squad(Long id) {
    setId(id);
  }

  /**
   * Adding a non-null-player with existiting person.
   */
  public void addPlayer(Player player) throws IllegalArgumentException {
    if (player == null || player.getPerson() == null) {
      throw new IllegalArgumentException("player oder person of player must not be null");
    }

    players.add(player);
  }

  public void removePlayer(Player p) {
    players.remove(p);
  }

  public void addPlayers(Collection<Player> players) {
    players.forEach(this::addPlayer);
  }

  @Override
  public Collection<Player> getPlayers() {
    return players;
  }

  public void setPlayers(Set<Player> newPlayers) {
    this.players = newPlayers;
  }

  public OpponentType getOpponentType() {
    return OpponentType.SQUAD;
  }

  @Override
  public String toString() {
    var personsString = new StringBuilder();
    for (var p : getPlayers()) {
      personsString.append(p.toString());
    }

    return "Squad [" + getId() + "] players:{" + personsString + "}";
  }
}
