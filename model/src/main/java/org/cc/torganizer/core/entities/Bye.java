package org.cc.torganizer.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Freilos.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Bye")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "BYES")
@PrimaryKeyJoinColumn(name = "BYE_ID")
public class Bye
  extends Opponent
  implements Serializable {

  /** serialVersionUID . */
  private static final long serialVersionUID = -5456818775828449905L;

  /**
   * Default.
   */
  public Bye() {
    // gem. Bean-Spec.
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<Player> getPlayers() {
    return Collections.EMPTY_LIST	;
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
    List<Opponent> byes = new ArrayList<Opponent>();
    for (int i = 0; i < size; i += 1) {
      Bye bye = new Bye();
      byes.add(bye);
    }

    return byes;
  }
}
