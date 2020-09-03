package org.cc.torganizer.core.entities;

/**
 * Interface für Entities, die sortiert werden müssen. Als Namen entfallen "index" und "order",
 * da dies gesperrte Schlüsselworte für Datenbanken sein können (z.B. MySQL).
 */
public interface Positional {
  /**
   * Gibt die Position zurück, mit dem eine Sortierung möglich wird.
   *
   * @return Position.
   */
  Integer getPosition();
}
