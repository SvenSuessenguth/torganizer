package org.cc.torganizer.core.system;

import java.util.List;

import org.cc.torganizer.core.entities.Match;

/**
 * Die verbleibenden Spiele werden je nach System aus den bisherigen
 * abgeschlossenen/laufenden Matches bestimmt. Laufende Matches werden dabei wie
 * abgeschlossene Matches behandelt.
 *
 * @author svens
 * @version $Id: $
 */
public interface PendingMatchDetector {

  /**
   * Ermitteln der verbleibenden Matches auf Grundlage der Opponents und der
   * bereits gespielten/laufenden Matches.
   *
   * @return Liste der verbleibenden Matches
   */
  List<Match> getPendingMatches();

}
