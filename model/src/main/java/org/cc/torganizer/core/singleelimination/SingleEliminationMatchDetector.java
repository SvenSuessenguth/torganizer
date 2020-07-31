package org.cc.torganizer.core.singleelimination;

import org.cc.torganizer.core.PendingMatchDetector;
import org.cc.torganizer.core.entities.System;
import org.cc.torganizer.core.entities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * In der ersten Spalte stehen die Indizes der Opponents, anschliessend die der
 * Matches.
 *
 * <pre>
 * Matches bei 16 Opponents
 *
 *       0       1      2      3   Level
 *
 * 0:1   7  -\
 *            |- 3 -\
 * 2:3   8  -/       |
 *                   |- 1 -\
 * 4:5   9  -\       |      |
 *            |- 4 -/       |
 * 6:7   10 -/              |
 *                          |- 0
 * 8:9   11 -\              |
 *            |- 5 -\       |
 * 10:11 12 -/       |      |
 *                   |- 2 -/
 * 12:13 13 -\       |
 *            |- 6 -/
 * 14:15 14 -/
 * </pre>
 * <table style="text-align: left;" border="0" cellpadding="0" cellspacing="0">
 * <caption>Sample</caption>
 * <tbody>
 * <tr>
 * <td style="vertical-align: middle; width: 1em;" rowspan="2">A <br>
 * </td>
 * <td style="width: 10px; vertical-align: middle;"><br>
 * </td>
 * <td style="border-bottom: 1px solid gray; width: 10px;" rowspan="2">&nbsp; <br>
 * </td>
 * <td style="vertical-align: middle; width: 1em;" rowspan="4">A</td>
 * <td style="width: 10px;" rowspan="2"><br>
 * </td>
 * <td style="border-bottom: 1px solid gray; width: 10px;" rowspan="4"><br>
 * </td>
 * <td style="vertical-align: middle; width: 1em;" rowspan="8">A<br>
 * </td>
 * </tr>
 * <tr>
 * <td style="border-top: 1px solid gray; border-right: 1px solid gray; vertical-align: middle;">
 * <br>
 * </td>
 * </tr>
 * <tr>
 * <td rowspan="2" style="vertical-align: middle;">B<br>
 * </td>
 * <td style="border-right: 1px solid gray; border-bottom: 1px solid gray; vertical-align: middle;">
 * <br>
 * </td>
 * <td rowspan="2" style="vertical-align: top;"><br>
 * </td>
 * <td style="border-top: 1px solid gray; border-right: 1px solid gray;
 * vertical-align: middle;" rowspan="2">
 * <br>
 * </td>
 * </tr>
 * <tr>
 * <td style="vertical-align: top;"><br>
 * </td>
 * </tr>
 * <tr>
 * <td rowspan="2" style="vertical-align: middle;">C<br>
 * </td>
 * <td style="vertical-align: top;"><br>
 * </td>
 * <td style="border-bottom: 1px solid gray; vertical-align: middle;" rowspan="2">
 * <br>
 * </td>
 * <td rowspan="4" style="vertical-align: middle;">C<br>
 * </td>
 * <td style="border-right: 1px solid gray; border-bottom: 1px solid gray;
 * vertical-align: middle;" rowspan="2">
 * <br>
 * </td>
 * <td rowspan="4" style="vertical-align: top;"><br>
 * </td>
 * </tr>
 * <tr>
 * <td style="border-top: 1px solid gray; border-right: 1px solid gray; vertical-align: middle;">
 * <br>
 * </td>
 * </tr>
 * <tr>
 * <td rowspan="2" style="vertical-align: middle;">D<br>
 * </td>
 * <td style="border-right: 1px solid gray; border-bottom: 1px solid gray; vertical-align: middle;">
 * <br>
 * </td>
 * <td rowspan="2" style="vertical-align: top;"><br>
 * </td>
 * <td style="border-top: 1px solid gray; vertical-align: middle;" rowspan="2"><br>
 * </td>
 * </tr>
 * <tr>
 * <td style="vertical-align: top;"><br>
 * </td>
 * </tr>
 * </tbody>
 * </table>
 */
public class SingleEliminationMatchDetector implements PendingMatchDetector {

  @Override
  public List<Match> getPendingMatches(Group group) {

    List<Match> pendingMatches = new ArrayList<>();

    // Index 0 fuer das Endspiel
    // Index 1 und 2 fuer die Halbfinalspiele
    // ...
    // Der Index wird von rechts nach links vergeben
    // Dadurch wird es einfach die Vorgaengerspiele zu ermitteln (i+1, i+2)
    // Anzahl der Matches im upper/winner bracket
    int m = group.getOpponents().size() - 1;
    int startIndexOnLevel0 = group.getOpponents().size() / 2 - 1;

    for (int index = 0; index < m; index += 1) {
      Match m0 = getUpperBracketsMatch(2 * index + 1, pendingMatches, group);
      Match m1 = getUpperBracketsMatch(2 * index + 2, pendingMatches, group);

      // Ein Match mit diesem Index existiert noch nicht
      if (group.getMatch(index) == null) {

        // Match findet nicht auf level-0 statt
        if (index < startIndexOnLevel0) {
          Match match = new Match(m0.getWinner(), m1.getWinner());
          match.setPosition(index);
          pendingMatches.add(match);
        }

        // Match hat keine Vorgaenger-Matches
        if (index >= startIndexOnLevel0) {
          Match match = new Match();
          match.setPosition(index);

          assignUpperBracketOpponentsToMatch(match, group);
          pendingMatches.add(match);
        }
      }
    }
    return pendingMatches;
  }

  /**
   * Suchen nach dem Match mit dem geforderten Index in der Liste der
   * uebergebenen Matches. Wird kein Match mit dem Index gefunden, wird ein
   * Match erzeugt, der Index gesetzt und die Opponents auf Unknown gesetzt.
   *
   * @param matchIndex Index des gesuchten Matches
   * @param matches    Liste der Matches, in der gesucht werden soll.
   * @return vorhandenes Match mit dem geforderten Index oder neues Match mit
   * unbekannten Opponents
   */
  private Match getUpperBracketsMatch(int matchIndex, List<Match> matches, Group group) {

    for (Match match : matches) {
      if (match.getPosition().equals(matchIndex)) {
        return match;
      }
    }

    for (Match match : group.getMatches()) {
      if (match.getPosition().equals(matchIndex)) {
        return match;
      }
    }

    return new Match(new Unknown(), new Unknown());
  }

  /**
   * Finden der Opponents eines Matches, welches keine Matches als Vorgaenger
   * hat. Die Opponents werden dann direkt aus der Liste aller der Group
   * zugewiesenen Opponents entnommen.
   *
   * @param match Match, dem die Opponents zugewiesen werden sollen
   */
  private void assignUpperBracketOpponentsToMatch(Match match, Group group) {

    int index = 2 * (match.getPosition() + 1) - group.getOpponents().size();

    Opponent home = group.getPositionalOpponent(index).getOpponent();
    Opponent guest = group.getPositionalOpponent(index + 1).getOpponent();

    match.setHome(home);
    match.setGuest(guest);
  }

  /**
   * Gibt an, wieviele Level bis zum Finale benoetigt werden.
   *
   * @return Anzahl der Level bis zum Finale
   */
  protected int getUpperBracketsNumberOfLevels(int groupSize) {

    // Gruppe hat Opponents
    long levels = Math.round(Math.log10(groupSize) / Math.log10(2));

    return (int) levels;
  }

  /**
   * Hier werden die Matches auf einem Level gefunden und nach ihrem Index
   * aufsteigende sortiert. Anschließend werden die Verlierer der Matches in
   * einer Liste an die gleiche Position wie das Match eingetragen. Beispiel:
   * Acht Opponents bestreiten auf Level 0 Die Matches 3, 4, 5 und 6. Der
   * Verlierer aus Match 6 kommt in der Liste der Loser auf Position 4, der
   * Verlierer aus Match 4 auf Position 2...
   *
   * @param level Level, zu dem die Loser gefunden werden sollen.
   * @return Liste der Loser. Diese Liste enthaelt auch NULL-Values, wenn ein
   * Match unbekannt ist oder noch nicht abgeschlossen ist.
   */
  protected final List<Opponent> getUpperBracketLosersOnLevel(int level, Group group) {
    List<Opponent> losersOnLevel = new ArrayList<>();
    List<Match> matchesOnLevel = getUpperBracketMatchesOnLevel(level, group);

    for (Match match : matchesOnLevel) {
      if (match != null) {
        losersOnLevel.add(match.getLoser());
      } else {
        losersOnLevel.add(null);
      }
    }

    return losersOnLevel;
  }

  /**
   * Ermitteln der Matches, die auf dem gegebenen Level stattgefunden haben.
   * Beispiel:
   *
   * <pre>
   * 64 Opponents (Indizes 0-63):
   * Level 0 -&gt; Matches 31 - 62
   * Level 1 -&gt; Matches 15 - 30
   * Level 2 -&gt; Matches 7 - 14
   * Level 3 -&gt; Matches 3 - 6
   * Level 4 -&gt; Matches 1 - 2
   * Level 5 -&gt; Matches 0 (Finale)
   * </pre>
   *
   * @param level Level
   * @return a {@link java.util.List} object.
   */
  List<Match> getUpperBracketMatchesOnLevel(int level, Group group) {
    List<Match> matches = new ArrayList<>();

    int groupSize = group.getOpponents().size();

    int startIndex = getUpperBracketStartIndex(level, groupSize);
    int endIndex = getUpperBracketEndIndex(level, groupSize);
    for (int index = startIndex; index <= endIndex; index += 1) {
      matches.add(group.getMatch(index));
    }

    return matches;
  }

  /**
   * Startindex der Matches zu einem gegebenen Level bestimmen (Siehe
   * Klassenkommentar).
   *
   * @param level Level
   * @return Startindex
   */
  int getUpperBracketStartIndex(int level, int groupSize) {
    double maxLevel = getUpperBracketsNumberOfLevels(groupSize);
    return (int) (Math.pow(2, maxLevel - level - 1) - 1);
  }

  /**
   * Endindex der Matches zu einem gegebenen Level bestimmen (Siehe
   * Klassenkommentar).
   *
   * @param level Level
   * @return Endindex
   */
  int getUpperBracketEndIndex(int level, int groupSize) {
    double maxLevel = getUpperBracketsNumberOfLevels(groupSize);
    return (int) (Math.pow(2, maxLevel - level) - 2);
  }

  @Override
  public System getSystem() {
    return System.SINGLE_ELIMINATION;
  }
}
