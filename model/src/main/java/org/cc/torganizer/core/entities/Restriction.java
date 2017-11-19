package org.cc.torganizer.core.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Abstrakte Oberklasse zu allen Restriktionen.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Restriction extends AbstractBaseEntity {

  @XmlAttribute
  private Long id;

  /**
   * Gibt an, ob es f\u00fcr den \u00fcbergebenen Opponent eine Restriktion
   * gibt, die eine Teilnahme verbietet.
   * 
   * @param opponent Opponent, der gepr\u00fcft werden soll.
   * @return <code>true</code>, wenn die Restriktionsregel erf\u00fcllt ist,
   *         sonst <code>false</code>
   */
  public abstract boolean isRestricted(Opponent opponent);

  @Override
  public final Long getId() {
    return id;
  }

  @Override
  public final void setId(Long newId) {
    id = newId;
  }
}
