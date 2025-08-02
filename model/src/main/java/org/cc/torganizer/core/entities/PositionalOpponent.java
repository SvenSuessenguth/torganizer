package org.cc.torganizer.core.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Opponent um einen Index erweitert, um in Listen eine Sortierreihenfolge
 * einhalten zu können.
 * Da ein Opponents an mehreren Stellen (Runden/Gruppen...) einsortiert sein kann, kann der
 * Sortierindex position keine Eigenschaft von Opponent sein.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PositionalOpponent extends Entity implements Positional {
  private Opponent opponent;
  private Integer position;

  /**
   * Tauschen der Position mit einem anderen PositionalOpponent.
   *
   * @param other {@link PositionalOpponent}, mit dem die Position getauscht werden soll.
   */
  public void swapPosition(PositionalOpponent other) {
    var thisPosition = getPosition();
    var otherPosition = other.getPosition();

    this.setPosition(otherPosition);
    other.setPosition(thisPosition);
  }
}
