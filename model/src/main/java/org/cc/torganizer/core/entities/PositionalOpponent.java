package org.cc.torganizer.core.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Opponent um einen Index erweitert, um in Listen eine Sortierreihenfolge
 * einhalten zu k\u00f6nnen.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "IndexedOpponent")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "POSITIONAL_OPPONENTS")
public class PositionalOpponent
  implements IPositional {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "POSITIONAL_OPPONENT_ID")
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "OPPONENT_ID")
  private Opponent opponent;

  @Column(name = "POSITION")
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

  /** {@inheritDoc} */
  public Long getId() {
    return id;
  }
  
  public void setId(Long newId) {
    this.id = newId;
  }
}
