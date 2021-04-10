package org.cc.torganizer.frontend.utils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.Collections;
import java.util.List;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Match;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Result;

@RequestScoped
@Named
public class Functions {

  /**
   * Cache mit den vorgerechneten Zweierpotenzen (power of two).
   */
  static final int[] POT_CACHE = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536,
      131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608};

  /**
   * Konstruktor.
   */
  private Functions() {
    // gem. Bean-Spec.
  }

  /**
   * Power of two.
   *
   * @param exponent Exponent
   * @return 2 hoch Exponent
   */
  public int getPot(int exponent) {
    return POT_CACHE[exponent];
  }

  /**
   * Finden eines Matches anhand des Index.
   *
   * @param group      Gruppe, in der gesucht werden soll.
   * @param matchIndex Index
   * @return Match mit dem geforderten Index oder <code>NULL</code>
   */
  public Match getMatchByIndex(Group group, int matchIndex) {
    if (group == null) {
      return null;
    }

    return group.getMatch(matchIndex);
  }

  /**
   * Finden eines Opponents anhand des Index.
   *
   * @param group         Group, in der gesucht werden soll
   * @param opponentIndex Index
   * @return Opponent mit dem geforderten Index
   */
  public Opponent getOpponentByIndex(Group group, int opponentIndex) {
    if (group == null) {
      return null;
    }

    return group.getOpponent(opponentIndex);
  }

  /**
   * Gibt den Gewinner eines Matches zurueck (oder NULL).
   *
   * @param match Match
   * @return Gewinner
   */
  public Opponent winner(Match match) {

    Opponent winner;

    if (match == null || match.getWinner() == null) {
      winner = new Player();
    } else {
      winner = match.getWinner();
    }

    return winner;
  }

  /**
   * Gibt die Results eines Matches zurueck.
   *
   * @param match Match
   * @return Liste mit den Results.
   */
  @SuppressWarnings("unchecked")
  public List<Result> results(Match match) {

    List<Result> results;

    if (match == null) {
      results = Collections.emptyList();
    } else {
      results = match.getResults();
    }

    return results;
  }

  /**
   * Gibt ein Array mit der gefordertden Groesse zurueck. Dieses kann in den
   * XHTML genutzt werden um mit einer forEach-Anweisung eine for-Loop zu
   * simulieren.
   *
   * @param size Groesse des geforderten Arrays.
   * @return Liste
   */
  public Object[] getArray(int size) {
    int arraySize = size;
    if (size < 0) {
      arraySize = 0;
    }
    return new Object[arraySize];
  }
}
