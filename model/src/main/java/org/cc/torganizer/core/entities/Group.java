package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.cc.torganizer.core.comparators.PositionalComparator;

/**
 * Eine Group sammelt Opponents zusammen. Innerhalb einer Group werden die
 * Matches ausgef\u00fchrt, die den Sieger oder die Opponents, die in die
 * n\u00e4chste Round kommen, bestimmen
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Group")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "GROUPS")
public class Group
  implements IPositional {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "GROUP_ID")
  private Long id;

  @Column(name = "INDEX")
  private Integer index;

  @OneToMany(cascade = CascadeType.ALL)
  @OrderBy("index ASC")
  @JoinTable(name = "GROUPS_INDEXED_OPPONENTS", joinColumns = {@JoinColumn(name = "GROUP_ID") }, inverseJoinColumns = @JoinColumn(name = "INDEXED_OPPONENT_ID"))
  private List<PositionalOpponent> indexedOpponents = new ArrayList<>();

  /**
   * Ein Match soll nur der Group zugewiesen werden, wenn es persistiert werden
   * soll (running/finished).
   */
  @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
  private List<Match> matches = new ArrayList<>();

  /**
   * Default.
   */
  public Group() {
    // gem. Bean-Spec.
  }

  /**
   * Gibt die nach Index sortierte Liste von Opponents zur\u00fcck.
   * 
   * @return nach Index sortierte Liste von IndexedOpponents
   */
  public List<PositionalOpponent> getIndexedOpponents() {
    // sortieren nach index
    Collections.sort(indexedOpponents, new PositionalComparator());

    return indexedOpponents;
  }

  /**
   * <p>
   * Setter for the field <code>indexedOpponents</code>.
   * </p>
   * 
   * @param inIndexedOpponents a {@link java.util.List} object.
   */
  public void setIndexedOpponents(List<PositionalOpponent> inIndexedOpponents) {
    this.indexedOpponents = inIndexedOpponents;
  }

  /**
   * Liste der Opponents, die zu dieser Group geh\u00f6ren wird neu gesetzt.
   * Alte Inhalte werden dabei gel\u00f6scht.
   * 
   * @param inOpponents Opponents, die der Group zugewiesen werden sollen.
   */
  public void setOpponents(List<Opponent> inOpponents) {
    getIndexedOpponents().clear();
    for (Opponent o : inOpponents) {
      PositionalOpponent io = new PositionalOpponent(o, inOpponents.indexOf(o) + 1);
      getIndexedOpponents().add(io);
    }
  }

  /**
   * Lesen der Opponents, die dieser Group zugewiesen sind. Die Liste
   * enth\u00e4lt die Opponents in der durch den Index vorgegebenen Reihenfolge.
   * 
   * @return Liste der Opponents, die dieser Gruppe zugewiesen sind.
   */
  public List<Opponent> getOpponents() {
    List<Opponent> result = new ArrayList<>();
    Collections.sort(getIndexedOpponents(), new PositionalComparator());

    for (PositionalOpponent io : getIndexedOpponents()) {
      result.add(io.getOpponent());
    }

    return result;
  }

  /**
   * Hinzuf\u00fcgen eines (unindizierten) Opponents. Der neue Index ist so
   * gro\u00df, dass der neue Opponent als letzter angef\u00fcgt wird.
   * 
   * @param opponent Opponent, der de Group hinzugef\u00fcgt werden soll.
   */
  public void addOpponent(Opponent opponent) {
    Integer oIndex = indexedOpponents.size();
    PositionalOpponent indexedOpponent = new PositionalOpponent();
    indexedOpponent.setPosition(oIndex);
    indexedOpponent.setOpponent(opponent);

    getIndexedOpponents().add(indexedOpponent);
  }

  /**
   * Entfernen eines Opponents aus der Liste aller (indizierten) Opponents.
   * 
   * @param opponent Opponent, der entfernt werden soll.
   */
  public void removeOpponent(Opponent opponent) {

    // passenden IndexedOpponent finden und entfernen
    PositionalOpponent ioToRemove = getIndexedOpponent(opponent);
    getIndexedOpponents().remove(ioToRemove);

    // Neuindizierung
    int newIndex = 0;
    for (PositionalOpponent o : getIndexedOpponents()) {
      o.setPosition(newIndex);
      newIndex += 1;
    }
  }

  /**
   * Lesen des indizierten Opponents, bei dem der Opponent mit dem Parameter
   * \u00fcbereinstimmt.
   * 
   * @param opponent Opponent, zu dem der indizierte Opponent gefunden werden
   *          soll.
   * @return Der IndexedOpponent
   */
  public PositionalOpponent getIndexedOpponent(Opponent opponent) {
    PositionalOpponent indexedOpponent = null;
    for (PositionalOpponent io : getIndexedOpponents()) {
      if (io.getOpponent().equals(opponent)) {
        indexedOpponent = io;
      }
    }
    return indexedOpponent;
  }

  /**
   * Gibt den Opponents zurueck, der in den entsprechenden Index hat.
   * 
   * @param opponentsIndex Index des gesuchten Opopnents.
   * @return a {@link org.cc.torganizer.core.entities.Opponent} object.
   */
  public Opponent getOpponent(Integer opponentsIndex) {
    PositionalOpponent io = getIndexedOpponent(opponentsIndex);
    return io == null ? null : io.getOpponent();
  }

  /**
   * Lesen des indizierten Opponents, bei dem der Index mit dem Parameter
   * \u00fcbereinstimmt.
   * 
   * @param reqIndex Index, zu dem der indizierte Opponent gefunden werden soll.
   * @return Der IndexedOpponent
   */
  public PositionalOpponent getIndexedOpponent(Integer reqIndex) {
    PositionalOpponent indexedOpponent = null;
    for (PositionalOpponent io : getIndexedOpponents()) {
      if (io != null && io.getPosition().equals(reqIndex)) {
        indexedOpponent = io;
      }
    }
    return indexedOpponent;
  }

  /**
   * \u00c4ndern der Reihenfolge innerhalb der Liste der indizierten Opponents
   * durch tauschen des Index, so dass der \u00fcbergebene Opponent den
   * n\u00e4chst kleineren Index bekommt.
   * 
   * @param opponent Opponent, der in der indexreihenfolge weiter vorne stehen
   *          soll (kleinerer Index)
   */
  public void moveUp(Opponent opponent) {
    PositionalOpponent indexedOpponent = getIndexedOpponent(opponent);
    int indexOld = indexedOpponent.getPosition();

    if (indexOld > 0) {
      PositionalOpponent swap = getIndexedOpponent(indexOld - 1);
      indexedOpponent.swapPosition(swap);
    }
  }

  /**
   * Tauschen der Position dieses Opponents mit dem Opponent, der den um eins
   * hoeheren Index hat (wenn moeglich).
   * 
   * @param opponent Opponent, der in der Positionsliste um eins nach unten
   *          wandern soll
   */
  public void moveDown(Opponent opponent) {
    PositionalOpponent indexedOpponent = getIndexedOpponent(opponent);
    int indexOld = indexedOpponent.getPosition();

    if (indexOld <= getIndexedOpponents().size() - 2) {
      PositionalOpponent swap = getIndexedOpponent(indexOld + 1);
      indexedOpponent.swapPosition(swap);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Integer getPosition() {
    return index;
  }

  public void setIndex(Integer newIndex) {
    this.index = newIndex;
  }

  /**
   * <p>
   * Getter for the field <code>matches</code>.
   * </p>
   * 
   * @return a {@link java.util.List} object.
   */
  public List<Match> getMatches() {
    return matches;
  }

  /**
   * Liste aller laufenden Matches.
   * 
   * @return Liste aller laufenden Matches
   */
  public List<Match> getRunningMatches() {
    List<Match> runningMatches = new ArrayList<>();

    for (Match match : getMatches()) {
      if (!match.isFinished()) {
        runningMatches.add(match);
      }
    }

    return runningMatches;
  }

  /**
   * Liste aller abgeschlossenen Matches.
   * 
   * @return Liste aller abgeschlossenen Matches
   */
  public List<Match> getFinishedMatches() {
    List<Match> finishedMatches = new ArrayList<>();

    for (Match match : getMatches()) {
      if (match.isFinished()) {
        finishedMatches.add(match);
      }
    }

    return finishedMatches;
  }

  /**
   * <p>
   * Setter for the field <code>matches</code>.
   * </p>
   * 
   * @param newMatches a {@link java.util.List} object.
   */
  public void setMatches(List<Match> newMatches) {
    this.matches = newMatches;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "[index:" + index + "][matches:" + getMatches().size() + "][indexedOpponents:" + indexedOpponents.size()
        + "]";
  }
  
  /**
   * Match mit dem gewuenschten Index. Der indes ist insbesondere fuer das
   * finden der noch ausstehenden Matches von Bedeutung.
   * 
   * @param matchIndex Indes des gesuchten Matches
   * @return a {@link org.cc.torganizer.core.entities.Match} object.
   */
  public Match getMatch(Integer matchIndex) {
    for (Match match : getMatches()) {
      if (match != null && matchIndex.equals(match.getPosition())) {
        return match;
      }
    }
    return null;
  }

  /**
   * <p>
   * Getter for the field <code>id</code>.
   * </p>
   * 
   * @return a {@link java.lang.Long} object.
   */
  public Long getId() {
    return id;
  }
}
