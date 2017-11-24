package org.cc.torganizer.core.system;

import java.util.ArrayList;
import java.util.List;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;

/**
 * RoundRobin bedeutet jeder spielt gegen jeden.
 */
public class RoundRobinMatchDetector
  extends AbstractPendingMatchDetector
  implements PendingMatchDetector {

  /**
   * Gruppe muss vorgegeben sein.
   * 
   * @param group Group
   */
  public RoundRobinMatchDetector(Group group) {
    super(group);
  }

  /** {@inheritDoc} */
  @Override
  public List<Match> getPendingMatches() {
    List<Match> pendingMatches = new ArrayList<>();

    // Alle spielen gegen alle
    // ohne Hin- und Rueckspiel 
    List<Opponent> opponents = getGroup().getOpponents();
    int n = opponents.size();
    int row = 0;
    for (Opponent home : opponents) {
      int col = 0;
      for (Opponent guest : opponents) {
        int matchIndex = row * n + col;
        if (row < col && getGroup().getMatch(matchIndex) == null) {
          Match match = new Match(home, guest);
          match.setPosition(matchIndex);
          pendingMatches.add(match);
        }
        col += 1;
      }
      row += 1;
    }

    return pendingMatches;
  }
}