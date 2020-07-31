package org.cc.torganizer.core.entities;

/**
 * Interface f[r Entities, die sortiert werden müssen. Als Namen entfallen "index" und "order",
 * da dies gesperrte Schlüsselworte für Datenbanken sein können (z.B. MySQL).
 */
public interface IPositional {
  /**
   * Gibt den Index zur\u00fcck, mit dem eine Sortierung m\u00f6glich wird.
   *
   * @return Index.
   */
  Integer getPosition();
}
