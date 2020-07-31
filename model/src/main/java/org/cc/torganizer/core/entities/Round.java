package org.cc.torganizer.core.entities;

import org.cc.torganizer.core.comparators.PositionalComparator;

import java.util.*;

/**
 * Runde in einer Disziplin. Alle Runden m\u00fcssen in einer definierten
 * Reihenfolge gespielt werden, daher der INDEX. Analog im Fussball gibt eine
 * Qualifikationsrunde, Vorrunde und Finalrunde. Im Badminton wird haeufig in
 * der ersten Runde Gruppenspiele gefolgt von Einfachem KO gespielt.
 */

public class Round extends Entity implements IPositional {

  private Integer position;

  private List<Group> groups = new ArrayList<>();

  /**
   * Every Round has a defaultsystem. Defaultsystem: ROUND_ROBIN
   */
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

  public Round(Long id) {
    setId(id);
  }

  /**
   * Add Group to round and set position.
   *
   * @param group Group
   */
  public void appendGroup(Group group) {
    group.setPosition(groups.size());
    groups.add(group);

    groups.sort(new PositionalComparator());
  }

  /**
   * Deleting group from round.
   */
  public void removeGroup(Group group) {
    groups.remove(group);
  }

  public List<Group> getGroups() {
    return Collections.unmodifiableList(groups);
  }

  public void setGroups(List<Group> newGroups) {
    this.groups = newGroups;
  }

  /**
   * Lesen der Group an der Stelle mit der geforderten Position aus der List aller
   * Groups.
   *
   * @param position Position der Group in der Liste aller Groups.
   * @return Groups an der Stelle mit dem Index
   */
  public Group getGroup(Integer position) {
    for (Group g : groups) {
      if (Objects.equals(g.getPosition(), position)) {
        return g;
      }
    }

    return null;
  }

  /**
   * Get rounds, which can be deleted. A round can be deleted, if it contains no opponent.
   */
  public List<Group> getDeletableGroups() {
    List<Group> deletableGroups = new ArrayList<>();

    for (Group group : groups) {
      if (group.getOpponents().isEmpty()) {
        deletableGroups.add(group);
      }
    }

    return deletableGroups;
  }

  public Set<Opponent> getQualifiedOpponents() {
    return Collections.emptySet();
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

  @Override
  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }
}