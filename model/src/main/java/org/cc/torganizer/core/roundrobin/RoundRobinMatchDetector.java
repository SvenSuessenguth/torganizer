package org.cc.torganizer.core.roundrobin;

import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.PendingMatchDetector;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.System;

/**
 * RoundRobin bedeutet jeder spielt gegen jeden.
 */
public class RoundRobinMatchDetector implements PendingMatchDetector {

  @Override
  public List<Match> getPendingMatches(Group group) {
    List<Match> pendingMatches = new ArrayList<>();

    // Alle spielen gegen alle
    // ohne Hin- und Rueckspiel 
    List<Opponent> opponents = group.getOpponents();
    int n = opponents.size();
    var row = 0;
    for (Opponent home : opponents) {
      var col = 0;
      for (Opponent guest : opponents) {
        int matchIndex = row * n + col;
        if (row < col && group.getMatch(matchIndex) == null) {
          var match = new Match(home, guest);
          match.setPosition(matchIndex);
          pendingMatches.add(match);
        }
        col += 1;
      }
      row += 1;
    }

    return pendingMatches;
  }

  @Override
  public System getSystem() {
    return System.ROUND_ROBIN;
  }
}