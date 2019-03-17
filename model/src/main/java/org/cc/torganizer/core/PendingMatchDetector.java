package org.cc.torganizer.core;

import java.util.List;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.System;

/**
 * Die verbleibenden Spiele werden je nach System aus den bisherigen
 * abgeschlossenen/laufenden Matches bestimmt. Laufende Matches werden dabei wie
 * abgeschlossene Matches behandelt.
 */
public interface PendingMatchDetector {

  /**
   * Ermitteln der verbleibenden Matches auf Grundlage der Opponents und der
   * bereits gespielten/laufenden Matches.
   *
   * @return Liste der verbleibenden Matches
   */
  List<Match> getPendingMatches(Group group);

  System getSystem();

}
