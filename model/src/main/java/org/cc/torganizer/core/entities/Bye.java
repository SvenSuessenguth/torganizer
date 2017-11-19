package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Freilos.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "bye")
@XmlAccessorType(XmlAccessType.FIELD)
public class Bye
  extends Opponent {

  /**
   * Default.
   */
  public Bye() {
    // gem. Bean-Spec.
  }

  @Override
  public List<Player> getPlayers() {
    return Collections.emptyList();
  }

  /**
   * Hilfsmethode zur Erzeugung einer Liste von Freilosen, die eine Assoziation
   * zum Turnier halten.
   * 
   * @param size Anzahl der zu erzeugenden Freilose
   * @param pTournament Turnier, in dem die Freilose eingesetzt werden.
   * @return Liste von Freilosen
   */
  public static List<Opponent> createByes(int size, Tournament pTournament) {

    // fehlenden Byes generieren
    List<Opponent> byes = new ArrayList<>();
    for (int i = 0; i < size; i += 1) {
      Bye bye = new Bye();
      byes.add(bye);
    }

    return byes;
  }
}
