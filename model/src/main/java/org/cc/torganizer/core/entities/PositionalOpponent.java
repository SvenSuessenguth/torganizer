package org.cc.torganizer.core.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Opponent um einen Index erweitert, um in Listen eine Sortierreihenfolge
 * einhalten zu k\u00f6nnen.
 */
@XmlRootElement(name = "IndexedOpponent")
@XmlAccessorType(XmlAccessType.FIELD)

public class PositionalOpponent extends Entity implements IPositional {
 
  private Opponent opponent;

  private Integer position;

  /**
   * default bean constructor.
   */
  public PositionalOpponent() {
    // gem. Bean-Spec.
  }

  /**
   * Bequemlichkeitskonstruktor.
   * 
   * @param newOpponent Opponent
   * @param newPosition Position des Opponents
   */
  public PositionalOpponent(Opponent newOpponent, Integer newPosition) {
    this.opponent = newOpponent;
    this.position = newPosition;
  }

  /**
   * Tauschen der Position mit einem anderen PositionalOpponent.
   * 
   * @param other {@link PositionalOpponent}, mit dem die Position getauscht werden soll.
   */
  public void swapPosition(PositionalOpponent other) {
    int thisPosition = getPosition();
    int otherPosition = other.getPosition();

    this.setPosition(otherPosition);
    other.setPosition(thisPosition);
  }

  public Opponent getOpponent() {
    return opponent;
  }

  public void setOpponent(Opponent inOpponent) {
    this.opponent = inOpponent;
  }

  /** {@inheritDoc} */
  @Override
  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer newPosition) {
    this.position = newPosition;
  }
}
