package org.cc.torganizer.core.roundrobin;

import jakarta.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.PendingMatchDetector;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.System;

/**
 * RoundRobin bedeutet jeder spielt gegen jeden.
 */
@RequestScoped
public class RoundRobinMatchDetector implements PendingMatchDetector {

  @Override
  public List<Match> getPendingMatches(Group group) {
    var pendingMatches = new ArrayList<Match>();

    // Alle spielen gegen alle
    // ohne Hin- und Rueckspiel 
    var opponents = group.getOpponents();
    var n = opponents.size();
    var row = 0;
    for (var home : opponents) {
      var col = 0;
      for (var guest : opponents) {
        var matchIndex = row * n + col;
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