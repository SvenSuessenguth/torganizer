package org.cc.torganizer.core.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Ein Squad ist eine Zusammenfassung mehrerer Players. Dies kann z.B. im
 * Badminton oder Tennis ein Doppel sein.
 */
public class Squad extends Opponent {

  /**
   * Ersatz f\u00fcr null-Values.
   */
  public static final Squad NONE = new Squad();

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

  public void addPlayer(Player player) {
    players.add(player);
  }

  public void removePlayer(Player p) {
    players.remove(p);
  }

  public void addPlayers(Collection<Player> players) {
    this.players.addAll(players);
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
    var personsString = new StringBuilder("");
    for (Player p : getPlayers()) {
      personsString.append(p.toString());
    }

    return "Squad [" + getId() + "] players:{" + personsString.toString() + "}";
  }
}
