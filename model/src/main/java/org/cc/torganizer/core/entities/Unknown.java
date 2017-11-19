package org.cc.torganizer.core.entities;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Wird verwendet, wenn die Gegner eines Matches noch nicht feststehen.
 * 
 * @author svens
 */
@XmlRootElement(name = "Unknown")
@XmlAccessorType(XmlAccessType.FIELD)
public class Unknown extends Opponent {

  /**
   * Default.
   */
  public Unknown() {
    // gem. Bean-Spec.
  }

  /** {@inheritDoc} */
  @Override
  public List<Player> getPlayers() {
    return Collections.emptyList();
  }
}
