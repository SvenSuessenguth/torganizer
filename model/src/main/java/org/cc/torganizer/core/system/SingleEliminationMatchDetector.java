package org.cc.torganizer.core.system;

import java.util.ArrayList;
import java.util.List;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Unknown;

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
 * <td style="border-top: 1px solid gray; border-right: 1px solid gray; vertical-align: middle;" rowspan="2">
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
 * <td style="border-right: 1px solid gray; border-bottom: 1px solid gray; vertical-align: middle;" rowspan="2">
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
 * 
 * @author svens
 * @version $Id: $
 */
public class SingleEliminationMatchDetector
  extends AbstractPendingMatchDetector
  implements PendingMatchDetector {

  /**
   * Konstruktor mit Angabe zu welcher Group die Berechnung erfolgen soll.
   * 
   * @param group Gruppe, auf die die Regeln angewendet werden sollen.
   */
  public SingleEliminationMatchDetector(Group group) {
    super(group);
  }

  /** {@inheritDoc} */
  @Override
  public List<Match> getPendingMatches() {

    List<Match> pendingMatches = new ArrayList<Match>();

    // Index 0 fuer das Endspiel
    // Index 1 und 2 fuer die Halbfinalspiele
    // ...
    // Der Index wird von rechts nach links vergeben
    // Dadurch wird es einfach die Vorgaengerspiele zu ermitteln (i+1, i+2)
    // Anzahl der Matches im upper/winner bracket
    int m = getGroup().getOpponents().size() - 1;
    int startIndexOnLevel0 = getGroup().getOpponents().size() / 2 - 1;

    for (int index = 0; index < m; index += 1) {
      Match m0 = getMatch(2 * index + 1, pendingMatches);
      Match m1 = getMatch(2 * index + 2, pendingMatches);

      // Ein Match mit diesem Index existiert noch nicht
      if (getGroup().getMatch(index) == null) {

        // Match findet nicht auf level-0 statt
        if (index < startIndexOnLevel0) {
          Match match = new Match(getGroup(), m0.getWinner(), m1.getWinner());
          match.setIndex(index);
          pendingMatches.add(match);
        }

        // Match hat keine Vorgaenger-Matches
        if (index >= startIndexOnLevel0) {
          Match match = new Match();
          match.setGroup(getGroup());
          match.setIndex(index);

          assignOpponentsToMatch(match);
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
   * @param matches Liste der Matches, in der gesucht werden soll.
   * @return vorhandenes Match mit dem geforderten Index oder neues Match mit
   *         unbekannten Opponents
   */
  protected Match getMatch(int matchIndex, List<Match> matches) {

    for (Match match : matches) {
      if (match.getIndex().equals(matchIndex)) {
        return match;
      }
    }

    for (Match match : getGroup().getMatches()) {
      if (match.getIndex().equals(matchIndex)) {
        return match;
      }
    }

    return new Match(getGroup(), new Unknown(), new Unknown());
  }

  /**
   * Finden der Opponents eines Matches, welches keine Matches als Vorgaenger
   * hat. Die Opponents werden dann direkt aus der Liste aller der Group
   * zugewiesenen Opponents entnommen.
   * 
   * @param match Match, dem die Opponents zugewiesen werden sollen
   */
  protected void assignOpponentsToMatch(Match match) {

    int oIndex = 2 * (match.getIndex() + 1) - getGroup().getOpponents().size();

    Opponent home = getGroup().getIndexedOpponent(oIndex).getOpponent();
    Opponent guest = getGroup().getIndexedOpponent(oIndex + 1).getOpponent();

    match.setHome(home);
    match.setGuest(guest);
  }

  /**
   * Wieviele Level werden bis zum Finale benoetigt?
   * 
   * @return Anzahl der Level bis zum Finale
   */
  public int getNumberOfLevels() {
    // Gruppe ist null oder keine Opponents
    if (getGroup() == null || getGroup().getOpponents().isEmpty()) {
      return 0;
    }

    // Gruppe hat Opponents
    int n = getGroup().getOpponents().size();
    long levels = Math.round(Math.log10(n) / Math.log10(2));

    return (int) levels;
  }

  /**
   * Hier werden die Matches auf einem Level gefunden und nach ihrem Index
   * aufsteigende sortiert. Anschlie�end werden die Verlierer der Matches in
   * einer Liste an die gleiche Position wie das Match eingetragen. Beispiel:
   * Acht Opponents bestreiten auf Level 0 Die Matches 3, 4, 5 und 6. Der
   * Verlierer aus Match 6 kommt in der Liste der Loser auf Position 4, der
   * Verlierer aus Match 4 auf Position 2...
   * 
   * @param level Level, zu dem die Loser gefunden werden sollen.
   * @return Liste der Loser. Diese Liste enthaelt auch NULL-Values, wenn ein
   *         Match unbekannt ist oder noch nicht abgeschlossen ist.
   */
  protected final List<Opponent> getLosersOnLevel(int level) {
    List<Opponent> losersOnLevel = new ArrayList<Opponent>();
    List<Match> matchesOnLevel = getMatchesOnLevel(level);

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
   * Level 0 -> Matches 31 - 62
   * Level 1 -> Matches 15 - 30
   * Level 2 -> Matches 7 - 14
   * Level 3 -> Matches 3 - 6
   * Level 4 -> Matches 1 - 2
   * Level 5 -> Matches 0 (Finale)
   * </pre>
   * 
   * @param level Level
   * @return a {@link java.util.List} object.
   */
  protected List<Match> getMatchesOnLevel(int level) {
    List<Match> matches = new ArrayList<Match>();

    int startIndex = getStartIndex(level);
    int endIndex = getEndIndex(level);
    for (int index = startIndex; index <= endIndex; index += 1) {
      matches.add(getGroup().getMatch(index));
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
  public int getStartIndex(int level) {
    int maxLevel = getNumberOfLevels();
    return (int) (Math.pow(2, maxLevel - level - 1) - 1);
  }

  /**
   * Endindex der Matches zu einem gegebenen Level bestimmen (Siehe
   * Klassenkommentar).
   * 
   * @param level Level
   * @return Endindex
   */
  public int getEndIndex(int level) {
    int maxLevel = getNumberOfLevels();
    return (int) (Math.pow(2, maxLevel - level) - 2);
  }
}