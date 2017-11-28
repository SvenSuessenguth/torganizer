package org.cc.torganizer.core.entities;

/**
 * Diese klasse dieent nur der Verwaltung der ID, die zur Persistierung mit JPA benötigt wird.
 */
public class Entity {
  private Long id;

  public Entity() {
    // gem. Bean-Spec.
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
