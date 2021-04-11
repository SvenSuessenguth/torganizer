package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.cc.torganizer.core.comparators.PositionalComparator;

/**
 * Runde in einer Disziplin. Alle Runden m\u00fcssen in einer definierten
 * Reihenfolge gespielt werden, daher der INDEX. Analog im Fussball gibt eine
 * Qualifikationsrunde, Vorrunde und Finalrunde. Im Badminton wird haeufig in
 * der ersten Runde Gruppenspiele gefolgt von Einfachem KO gespielt.
 */

public class Round extends Entity implements Positional {

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

  /**
   * Returns the groups of this round ordered bei their position.
   */
  public List<Group> getGroups() {
    groups.sort(new PositionalComparator());
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
   * Get groups, which can be deleted. A group can be deleted, if it contains no opponent.
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

  public Collection<Opponent> getQualifiedOpponents() {
    return Collections.emptyList();
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