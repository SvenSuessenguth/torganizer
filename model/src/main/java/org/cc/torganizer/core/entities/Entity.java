package org.cc.torganizer.core.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * Diese klasse dieent nur der Verwaltung der ID, die zur Persistierung mit JPA benötigt wird.
 */
public class Entity implements Serializable {
  private Long id;

  private Date lastUpdate;

  public Entity() {
    // gem. Bean-Spec.
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Getting a copy of the current lastUpdate.
   */
  public Date getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}
