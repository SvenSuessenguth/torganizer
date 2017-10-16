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

  /**
   * {@inheritDoc} Gleichheit wird durch die DB-ID festgestellt.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BaseEntity)) {
      return false;
    }

    BaseEntity other = (BaseEntity) obj;
    if (this.getId() == null || other.getId() == null) {
      return false;
    }
    return this.getId() != null && this.getId().equals(other.getId());
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return this.getId() == null ? super.hashCode() : this.getId().hashCode();
  }
}
