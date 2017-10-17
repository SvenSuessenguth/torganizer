package org.cc.torganizer.core.entities;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Geschlecht.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Gender")
@XmlAccessorType(XmlAccessType.FIELD)
public enum Gender implements Serializable {

  /** Maennlich. */
  MALE,

  /** Weiblich. */
  FEMALE,

  /** Unbekannt. */
  UNKNOWN;

  /** Position von MALE. */
  public static final int MALE_ORDINAL = 0;

  /** Position von FEMALE. */
  public static final int FEMALE_ORDINAL = 1;

  /** Position von UNKNOWN. */
  public static final int UNKNOWN_ORDINAL = 2;
}