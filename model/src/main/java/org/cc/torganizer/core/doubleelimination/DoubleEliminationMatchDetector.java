package org.cc.torganizer.core.doubleelimination;

import jakarta.enterprise.context.RequestScoped;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.PendingMatchDetector;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.System;
import org.cc.torganizer.core.singleelimination.SingleEliminationMatchDetector;
import org.cc.torganizer.core.util.Checker;

/**
 * Aufbau einer Turnierstruktur. In der ersten Spalte stehen die Opponents In
 * allen weiteren Spalten die Matches/deren Index fuer das Upper Bracket
 * (identisch zu SingelElimination) <br>
 * <br>
 * Beispiel eines Tournaments mit 16 Opponents:
 *
 * <pre>
 * Matches im Upper Bracket
 *
 * 0       1      2      3  Level
 *
 * 7  -\
 *      |- 3 -\
 * 8  -/       |
 *             |- 1 -\
 * 9  -\       |      |
 *      |- 4 -/       |
 * 10 -/              |
 *                    |- 0
 * 11 -\              |
 *      |- 5 -\       |
 * 12 -/       |      |
 *             |- 2 -/
 * 13 -\       |
 *      |- 6 -/
 * 14 -/
 * </pre>
 *
 * <pre>
 * Matches im Lower Bracket mit den Loser des Upper Bracket
 * In der Liste aller Matches haben die Matches im Lower Bracket einen Index,
 * der um die Anzahl der Matches im Upper Bracket hoeher liegt.
 *
 *
 * Prefix L meint den (neu sortierten) Verlierer aus dem Upper Bracket
 *
 *         0               1           2               3            4               5   Level
 *
 *                 L6 -\
 * L7 -\                |- 4 (19) -\           L1 -\
 *      |- 0 (15) -----/            |               |- 10 (25) -\
 * L8 -/                            |- 8 (23) -----/             |
 *                 L5 -\            |                            |          L0 -\
 * L9 -\                |- 5 (20) -/                             |               |- 13 (28)
 *      |- 1 (16) -----/                                         |- 12 (27) ----/
 * L10-/                                                         |
 *                 L4 -\                                         |
 * L11-\                |- 6 (21) -\           L2 -\             |
 *      |- 2 (17) -----/            |               |- 11 (26) -/
 * L12-/                            |- 9 (24) -----/
 *                 L3 -\            |
 * L13-\                |- 7 (22) -/
 *      |- 3 (18) -----/
 * L14-/
 * </pre>
 *
 * <p>Das Finale wird nur einfach gespielt, auch wenn der Verlierer des Finales aus
 * dem Upper-Bracket stammt und bisher noch nicht verloren hat.
 *
 * @see <a
 *     href="https://groups.google.com/forum/?fromgroups#!topic/rec.sport.table-soccer/CCUadSrQymk">rec.sport.table-soccer</a>
 */
@RequestScoped
public class DoubleEliminationMatchDetector extends SingleEliminationMatchDetector
    implements PendingMatchDetector {


  @Override
  public List<Match> getPendingMatches(Group group) {

    var pendingMatches = new ArrayList<Match>();

    // Upper und Lower Bracket
    var pendingMatchesUpperBracket = super.getPendingMatches(group);
    var pendingMatchesLowerBracket = getPendingMatchesLowerBracket(group);
    pendingMatches.addAll(pendingMatchesUpperBracket);
    pendingMatches.addAll(pendingMatchesLowerBracket);

    // Finale
    var finalMatch = getPendingFinalMatch(group);
    if (finalMatch != null) {
      pendingMatches.add(getPendingFinalMatch(group));
    }

    return pendingMatches;
  }

  /**
   * Das Finale wird aus dem Sieger des Upper Brackets und dem Sieger des Lower
   * Bracktes ermittelt.
   *
   * @return Finale, wenn noch nicht gespielt/beendet
   */
  Match getPendingFinalMatch(Group group) {

    Match finalMatch = null;
    int finalMatchIndex = 2 * (group.getOpponents().size() - 1) - 1;
    if (group.getMatch(finalMatchIndex) != null) {
      return null;
    }

    var wub = Match.getWinner(group.getMatch(0));
    var wlb = Match.getWinner(group.getMatch(finalMatchIndex - 1));

    if (Checker.isNoNullValue(wub, wlb)) {
      finalMatch = new Match(wub, wlb);
      finalMatch.setPosition(finalMatchIndex);
    }

    return finalMatch;
  }

  /**
   * Für das Lower-Bracket sind alle bereits gespielten Matches relevant (Upper
   * und Lower Bracket).
   *
   * @return Liste der noch ausstehenden Matches aus dem Lower Bracket
   */
  List<Match> getPendingMatchesLowerBracket(Group group) {
    // Hilfslisten
    var pendingMatches = new ArrayList<Match>();

    var groupSize = group.getOpponents().size();

    // Anzahl der Level
    var maxLevel = super.getUpperBracketsNumberOfLevels(groupSize) * 2L;
    // Matches der jeweiligen Level ermitteln
    for (var level = 0; level <= maxLevel; level += 1) {
      var matchesOnLevel = getMatchesOnLevel(level, group);
      for (var match : matchesOnLevel) {
        if (!match.isFinished()) {
          pendingMatches.add(match);
        }
      }
    }

    return pendingMatches;
  }

  /**
   * Gibt die Matches auf einem bestimmten Level im DoubleElimination-System
   * zurueck.
   *
   * @param level Level
   * @return Matches auf dem gegebenen Level
   */
  private List<Match> getMatchesOnLevel(int level, Group group) {

    var matches = new ArrayList<Match>();

    if (isFirstLevel(level)) {
      matches.addAll(getFirstLevelMatches(group));
    } else if (hasToMixUpperLowerBracket(level)) {
      // umsortierte Verlierer aus dem Level '(l+1)/2' aus dem Upper Bracket
      // mit den Gewinnern des Levels 'l-1' aus dem Lower Bracket
      var splitFactor = (int) (Math.pow(2.0, level));
      var tmp = super.getUpperBracketLosersOnLevel((level + 1) / 2, group);
      var ubl = orderUpperBracketLosers(tmp, splitFactor, 1);
      var lbw = getWinnersOnLevel(level - 1, group);

      // Das Upper und das Lower Bracket koennen unterschiedlich weit fortgeschritten sein
      // Daher die minimale Groesse der Listen verwenden
      for (var i = 0; i < Math.min(ubl.size(), lbw.size()); i += 1) {
        var home = ubl.get(i);
        var guest = lbw.get(i);
        addMatchToList(matches, createPendingMatch(level, i, home, guest, group));
      }

    } else {
      // Gewinner des Levels 'l-1' aus dem Lower Bracket
      var opponents = getWinnersOnLevel(level - 1, group);
      for (var i = 0; i < opponents.size() / 2; i += 2) {
        var home = opponents.get(i);
        var guest = opponents.get(i + 1);
        addMatchToList(matches, createPendingMatch(level, i, home, guest, group));
      }
    }

    return matches;
  }

  /**
   * Gibt die Matches auf Level-0 zurueck. Diese werden ausschliesslich aus den
   * Verlierern des Levels-0 aus dem Upper Bracket gebildet.
   *
   * @return Liste der Matches auf Level-0
   */
  List<Match> getFirstLevelMatches(Group group) {
    var level = 0;
    var matches = new ArrayList<Match>();
    var losers = super.getUpperBracketLosersOnLevel(level, group);
    for (var i = 0; i < losers.size() - 1; i += 2) {
      var home = losers.get(i);
      var guest = losers.get(i + 1);
      addMatchToList(matches, createPendingMatch(level, i / 2, home, guest, group));
    }

    return matches;
  }

  /**
   * Erzeugen eines pending Matches, wenn home und guest ungleich
   * <code>null</code> sind und das Match mit dem Index noch nicht existiert.
   *
   * @param level Level
   * @param i     Index des Matches auf dem Level
   * @param home  Heim
   * @param guest Gast
   * @return Match mit allen erforderlichen Parametern
   */
  Match createPendingMatch(int level, int i, Opponent home, Opponent guest, Group group) {
    Match match = null;
    var groupSize = group.getOpponents().size();

    var matchIndex = getMatchIndex(level, i, groupSize);
    if (Checker.isNoNullValue(home, guest) && group.getMatch(matchIndex) == null) {
      match = new Match(home, guest);
      match.setPosition(matchIndex);
    }

    return match;
  }

  /**
   * Das erste Level im Lower Bracket hat den Wert 0.
   *
   * @param level Level
   * @return <code>true</code>, wenn der uebergebene Wert 0 ist, sonst
   *     <code>false</code>
   */
  boolean isFirstLevel(int level) {
    return level == 0;
  }

  /**
   * Bei jedem zweiten, ungraden Level werden die Loser des Upper Brackets mit
   * den Winnern des Lower Brackets gemischt.
   *
   * @param level Level
   * @return <code>true</code>, wenn Oppoenents des Upper und des Lower Brackets
   *     gemischt werden.
   */
  boolean hasToMixUpperLowerBracket(int level) {
    return level % 2 != 0;
  }

  /**
   * Hinzufuegen eines Matches zu einer Liste von Matches, wenn das Match nicht
   * <code>null</code> ist.
   *
   * @param matches Liste von Matches
   * @param match   Match
   */
  void addMatchToList(List<Match> matches, Match match) {
    if (match != null) {
      matches.add(match);
    }
  }

  /**
   * Gibt den Index eines Matches zurueck.
   *
   * @param level      Level, auf dem das Match stattfindet
   * @param levelIndex Index innerhalb des Levels
   * @return Index des Matches unter Beruecksichtigung des Upper Brackets
   */
  int getMatchIndex(int level, int levelIndex, int groupSize) {
    var mub = countMatchesUpperBracket(groupSize);

    if (level == 0) {
      return mub + levelIndex;
    }

    int matchIndex = mub;
    matchIndex += countMatchesUpToLevel(level - 1, groupSize);
    matchIndex += levelIndex;

    return matchIndex;
  }

  /**
   * Anzahl der Matches im Upper Bracket als Ausgangspunkt fuer die Indizes der
   * Matches im Lower Bracket.
   *
   * @return Anzahl der Matches im Upper Bracket
   */
  private int countMatchesUpperBracket(int groupSize) {
    return groupSize - 1;
  }

  /**
   * Anzahl der Matches bis zum angegebenen Level (einschliesslich). Diese
   * Anzahl bezieht sich nur auf die Matches im Lower Bracket.
   *
   * @param level Level
   * @return Anzahl der Matches im Lower Bracket
   */
  int countMatchesUpToLevel(int level, int groupSize) {

    var number = 0;

    for (var i = 0; i <= level; i += 1) {
      number += countMatchesOnLevel(i, groupSize);
    }

    return number;
  }

  /**
   * Anzahl der Matches auf einem gegebenen Level.
   *
   * @param level Level
   * @return Anzahl der Matches auf diesem Level
   */
  int countMatchesOnLevel(int level, int groupSize) {

    // bei negativel Level wird 0 zurueckgegeben
    if (level < 0) {
      return 0;
    }

    // Anzahl Matches auf level 0
    // Anzahl der Opponents insgesamt = n

    // n / 2 Opponents nach dem ersten Level im Upper Bracket
    // n / 2 / 2 Matches (mit je zwei opponents!) auf Level 0 im Lower Bracket
    var start = groupSize / 2 / 2;

    // Beispiel 16 Opponents -> start = 4
    // level 0:  4/1
    // level 1:  4/1
    // level 2:  4/2
    // level 3:  4/2
    // level 4:  4/4
    // level 5:  4/4
    // der Divisor verdoppelt sich bei jedem Tupel

    var exponent = (level - (level % 2)) / 2.0;
    var divisor = Math.pow(2.0, exponent);
    var number = start / divisor;

    return (int) number;
  }

  /**
   * Liste der Gewinner auf einem Level.
   *
   * @param level Level
   * @return Liste der Oppoents, die die Matches auf diesem Level gewonnen
   *     haben. Diese Liste enthaelt auch NULL-Values, wenn ein Match
   *     unbekannt ist oder noch nicht abgeschlossen ist.
   */
  List<Opponent> getWinnersOnLevel(int level, Group group) {

    var winners = new ArrayList<Opponent>();
    var matches = getDoubleEliminationExistingMatchesOnLevel(level, group);

    for (var match : matches) {
      winners.add(Match.getWinner(match));
    }

    return winners;
  }

  /**
   * Gibt eine Liste der bereits extistierenden Matches zurueck, die auf einem
   * Level ausgefuehrt werden.
   *
   * @param level Level
   * @return Liste der bereits vorhandenen Matches auf einem Level
   */
  private List<Match> getDoubleEliminationExistingMatchesOnLevel(int level, Group group) {
    var matches = new ArrayList<Match>();
    var groupSize = group.getOpponents().size();

    var startMatchIndex = getStartMatchIndex(level, groupSize);
    var endMatchIndex = getEndMatchIndex(level, groupSize);

    for (var matchIndex = startMatchIndex; matchIndex <= endMatchIndex; matchIndex += 1) {
      var match = group.getMatch(matchIndex);
      if (match != null) {
        matches.add(match);
      }
    }

    return matches;
  }

  /**
   * Gibt den Index zurueck, bei dem Matches auf einem gegebenen Level starten.
   *
   * @param level Level
   * @return Index des ersten Matches auf dem Level
   */
  int getStartMatchIndex(int level, int groupSize) {
    // Index beginnt bei '0', daher beim Ergebnis des zaehlens jeweils 1 abziehen
    return countMatchesUpperBracket(groupSize)
        + countMatchesUpToLevel(level, groupSize)
        - countMatchesOnLevel(level, groupSize);
  }

  /**
   * Gibt den Index zurueck, bei dem Matches auf einem gegebenen Level enden.
   *
   * @param level Level
   * @return Index des letzten Matches auf dem Level
   */
  int getEndMatchIndex(int level, int groupSize) {
    // Index beginnt bei '0', daher beim Ergebnis des zaehlens 1 abziehen
    return getStartMatchIndex(level, groupSize) + countMatchesOnLevel(level, groupSize) - 1;
  }

  /**
   * Die Verlierer eines Levels im Upper Bracket werden umsortiert in das Lower
   * Bracket eingemischt, damit ein Aufeinandertreffen von Opponents vermieden
   * wird, die bereits im Upper Bracket gegeneinander gespielt haben. Der split-
   * und der reverseFactor muessen wegen des rekursiven Aufrufes uebergeben
   * werden.
   *
   * @param opponents     Liste der Opponents, die sortiert werden muessen
   * @param splitFactor   SplitFactor gem. s.o.
   * @param reverseFactor reverseFactor gem. s.o.
   * @return umsortierte Liste der Verlierer
   */
  List<Opponent> orderUpperBracketLosers(List<Opponent> opponents, int splitFactor,
                                         int reverseFactor) {

    if (splitFactor == 1) {
      return opponents;
    }

    var notReverseFactor = isFirstLevel(reverseFactor) ? 1 : 0;

    var left = opponents.subList(0, opponents.size() / 2);
    var right = opponents.subList(opponents.size() / 2, opponents.size());

    var result = new ArrayList<Opponent>();
    if (reverseFactor == 1) {
      result.addAll(orderUpperBracketLosers(right, splitFactor / 2, notReverseFactor));
      result.addAll(orderUpperBracketLosers(left, splitFactor / 2, notReverseFactor));
    } else {
      result.addAll(orderUpperBracketLosers(left, splitFactor / 2, notReverseFactor));
      result.addAll(orderUpperBracketLosers(right, splitFactor / 2, notReverseFactor));
    }
    return result;
  }

  @Override
  public System getSystem() {
    return System.DOUBLE_ELIMINATION;
  }
}
