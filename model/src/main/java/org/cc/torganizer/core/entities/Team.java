package org.cc.torganizer.core.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Ein Team ist eine Zusammenfassung mehrere Opponents. im Badminton sind das
 * z.B. Squads und Players.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Team extends Opponent {

  /**
   * Ersatz für null-Values.
   */
  public static final Team NONE = new Team();

  /**
   * Set of actually playing opponents, which can be players but Squads or other
   * Teams also. In Badminton there are playing single players and squads
   * (double e.g.).
   */
  private List<Opponent> opponents = new ArrayList<>();

  public void addOpponent(Opponent opponent) {
    opponents.add(opponent);
  }

  @Override
  public Set<Player> getPlayers() {
    var players = new HashSet<Player>();

    for (var opponent : opponents) {
      players.addAll(opponent.getPlayers());
    }

    return players;
  }

  @Override
  public OpponentType getOpponentType() {
    return OpponentType.TEAM;
  }
}