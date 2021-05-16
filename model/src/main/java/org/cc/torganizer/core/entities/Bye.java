package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Freilos.
 */
public class Bye extends Opponent {

  /**
   * Hilfsmethode zur Erzeugung einer Liste von Freilosen, die eine Assoziation
   * zum Turnier halten.
   *
   * @param size Anzahl der zu erzeugenden Freilose
   * @return Liste von Freilosen
   */
  static List<Opponent> createByes(int size) {

    // fehlenden Byes generieren
    List<Opponent> byes = new ArrayList<>();
    for (var i = 0; i < size; i += 1) {
      var bye = new Bye();
      byes.add(bye);
    }

    return byes;
  }

  @Override
  public Set<Player> getPlayers() {
    return Collections.emptySet();
  }

  @Override
  public OpponentType getOpponentType() {
    return null;
  }
}
