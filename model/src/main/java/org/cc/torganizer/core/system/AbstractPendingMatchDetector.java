package org.cc.torganizer.core.system;

import org.cc.torganizer.core.entities.Group;

/**
 * Dient zur Verwaltung der Group, zu der noch ausstehende Matches gefunden
 * werden sollen.
 */
public abstract class AbstractPendingMatchDetector
  implements PendingMatchDetector {

  private Group group;

  /**
   * Group muss vorhanden sein.
   *
   * @param newGroup Group
   */
  public AbstractPendingMatchDetector(Group newGroup) {
    this.group = newGroup;
  }

  /**
   * <p>Getter for the field <code>group</code>.</p>
   *
   * @return a {@link org.cc.torganizer.core.entities.Group} object.
   */
  protected Group getGroup() {
    return group;
  }

  /**
   * <p>Setter for the field <code>group</code>.</p>
   *
   * @param newGroup a {@link org.cc.torganizer.core.entities.Group} object.
   */
  protected void setGroup(Group newGroup) {
    this.group = newGroup;
  }
}
