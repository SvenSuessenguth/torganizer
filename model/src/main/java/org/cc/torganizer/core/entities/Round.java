package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Runde in einer Disziplin. Alle Runden m\u00fcssen in einer definierten
 * Reihenfolge gespielt werden, daher der INDEX. Analog im Fussball gibt eine
 * Qualifikationsrunde, Vorrunde und Finalrunde. Im Badminton wird haeufig in
 * der ersten Runde Gruppenspiele gefolgt von Einfachem KO gespielt.
 */

public class Round extends Entity implements IPositional {

  private Integer position;

  private List<Group> groups = new ArrayList<>();

  /** Every Round has a defaultsystem. Defaultsystem: ROUND_ROBIN */
  private System system = System.ROUND_ROBIN;

  /**
   * Number of players, which are qualified for the next round.
   */  
  private Integer qualified = 0;

  /**
   * Default.
   */
  public Round() {
    // gem. Bean-Spec.
  }

  public Round(Long id){
    setId(id);
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

  public Set<Opponent> getQualifiedOpponents() {
    return Collections.<Opponent>emptySet();
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

  public void setSystem(System system) {
    this.system = system;
  }

  public Integer getQualified() {
    return qualified;
  }

  public void setQualified(Integer qualified) {
    this.qualified = qualified;
  }

  /** {@inheritDoc} */
  @Override
  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }
}

@FunctionalInterface
interface QualifiedOpponentsCalculator{
  Set<Opponent> calculate(Round round);
}