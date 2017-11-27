package org.cc.torganizer.core.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Diese klasse dieent nur der Verwaltung der ID, die zur Persistierung mit JPA benötigt wird.
 */
@XmlRootElement(name="Entity")
@XmlAccessorType(XmlAccessType.FIELD)
public class Entity {
  @XmlElement
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
