package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Runde in einer Disziplin. Alle Runden m\u00fcssen in einer definierten
 * Reihenfolge gespielt werden, daher der INDEX. Analog im Fussball gibt eine
 * Qualifikationsrunde, Vorrunde und Finalrunde. Im Badminton wird haeufig in
 * der ersten Runde Gruppenspiele gefolgt von Einfachem KO gespielt.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Round")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "ROUNDS")
public class Round implements IPositional {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ROUND_ID")
  private Long id;

  @Column(name = "INDEX")
  private Integer index;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "ROUNDS_GROUPS", joinColumns = {
      @JoinColumn(name = "ROUND_ID") }, inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
  private List<Group> groups = new ArrayList<>();

  /** Every Round has a defaultsystem. Defaultsystem: ROUND_ROBIN */
  @Enumerated(EnumType.STRING)
  @Column(name = "SYSTEM", nullable = true)
  private System system = System.ROUND_ROBIN;

  /**
   * Anzahl der Spieler, die in die n\u00e4chste Runde kommen.
   */
  @Column(name = "FORWARD_PLAYERS_COUNT")
  private Integer forwardPlayersCount = Integer.valueOf(0);

  /**
   * Default.
   */
  public Round() {
    // gem. Bean-Spec.
  }

  /**
   * Hinzufuegen einer Group zu der Liste aller Groups.
   * 
   * @param group Group
   */
  public void addGroup(Group group) {
    groups.add(group);
  }

  public List<Group> getGroups() {
    return Collections.unmodifiableList(groups);
  }

  /**
   * Lesen der Group an der Stelle mit dem geforderten Indes aus der List aller
   * Groups.
   * 
   * @param inIndex Position der Group in der Liste aller Groups.
   * @return Groups an der Stelle mit dem Index
   */
  public Group getGroup(int inIndex) {
    return getGroups().get(inIndex);
  }

  public List<Opponent> getForwardPlayers() {
    // TODO: die besten 'forwardPlayersCount' Opponents einer Runde bestimmen
    return Collections.emptyList();
  }

  public Group getFirstGroup() {
    return getGroup(0);
  }

  public void setGroups(List<Group> newGroups) {
    this.groups = newGroups;
  }

  public int getGroupsCount() {
    return getGroups().size();
  }

  public System getSystem() {
    return system;
  }

  public void setSystem(System newSystem) {
    this.system = newSystem;
  }

  public Integer getForwardPlayersCount() {
    return forwardPlayersCount;
  }

  public void setForwardPlayersCount(Integer newForwardPlayersCount) {
    this.forwardPlayersCount = newForwardPlayersCount;
  }

  /** {@inheritDoc} */
  @Override
  public Integer getPosition() {
    return index;
  }

  public void setIndex(Integer newIndex) {
    this.index = newIndex;
  }
}
