package org.cc.torganizer.core.entities;

/**
 * Abstrakte Klasse zu den Entitaeten.
 * 
 * @author svens
 */
abstract class AbstractBaseEntity
  implements BaseEntity {

  /**
   * <p>
   * isTransient.
   * </p>
   * 
   * @return a boolean.
   */
  public boolean isTransient() {
    return getId() == null;
  }
}
