package org.cc.torganizer.core.comparators;

import java.io.Serializable;
import java.util.Comparator;
import org.cc.torganizer.core.entities.Match;

/**
 * Erm\u00F6glichen einer Sortierung, die fuer eine gleichmaessige Wartezeit der
 * Spieler sorgt.
 */
public class MatchIdleTimeComparator implements Comparator<Match>, Serializable {

  @Override
  public final int compare(final Match m1, final Match m2) {

    // durchschnittliche Wartezeit der Player ermitteln
    Long idleTime1 = m1 == null ? Long.valueOf(0L) : m1.getIdleTime();
    Long idleTime2 = m2 == null ? Long.valueOf(0L) : m2.getIdleTime();

    return idleTime2.compareTo(idleTime1);
  }
}
