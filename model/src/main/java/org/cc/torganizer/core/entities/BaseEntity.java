package org.cc.torganizer.core.entities;

/**
 * Schnittstelle, die jede Entitaet bereitstellen soll.
 *
 * @author svens
 * @version $Id: $
 */
public interface BaseEntity {

  /**
   * Getter zur DB-ID.
   *
   * @return ID
   */
  Long getId();
  
  void setId(Long newId);

  /**
   * Ist das Objekt persitent oder nicht.
   *
   * @return <code>true</code>, wenn das Objekt persistent ist, sonst
   *         <code>false</code>
   */
  boolean isTransient();

}
