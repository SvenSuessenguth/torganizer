package org.cc.torganizer.core.entities;

/**
 * Abstrakte Oberklasse zu allen Restriktionen.
 */
public abstract class Restriction extends Entity{

  /**
   * Gibt an, ob es f\u00fcr den \u00fcbergebenen Opponent eine Restriktion
   * gibt, die eine Teilnahme verbietet.
   * 
   * @param opponent Opponent, der gepr\u00fcft werden soll.
   * @return <code>true</code>, wenn die Restriktionsregel erf\u00fcllt ist,
   *         sonst <code>false</code>
   */
  public abstract boolean isRestricted(Opponent opponent);
}
