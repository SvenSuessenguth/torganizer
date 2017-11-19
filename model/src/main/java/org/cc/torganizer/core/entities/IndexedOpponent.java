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
@Table(name = "INDEXED_OPPONENTS")
public class IndexedOpponent
  extends AbstractBaseEntity
  implements IIndexed {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "INDEXED_OPPONENT_ID")
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "OPPONENT_ID")
  private Opponent opponent;

  @Column(name = "INDEX")
  private Integer index;

  /**
   * default bean constructor.
   */
  public IndexedOpponent() {
    // gem. Bean-Spec.
  }

  /**
   * Bequemlichkeitskonstruktor.
   * 
   * @param newOpponent Opponent
   * @param newIndex Index des Opponents
   */
  public IndexedOpponent(Opponent newOpponent, Integer newIndex) {
    this.opponent = newOpponent;
    this.index = newIndex;
  }

  /**
   * Tauschen des Index mit einem anderen IndexedOpponent.
   * 
   * @param other IdexedOpponent, mit dem der Index getauscht werden soll.
   */
  public void swapIndex(IndexedOpponent other) {
    int thisIndex = getIndex();
    int otherIndex = other.getIndex();

    this.setIndex(otherIndex);
    other.setIndex(thisIndex);
  }

  public Opponent getOpponent() {
    return opponent;
  }

  public void setOpponent(Opponent inOpponent) {
    this.opponent = inOpponent;
  }

  /** {@inheritDoc} */
  @Override
  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer inIndex) {
    this.index = inIndex;
  }

  /** {@inheritDoc} */
  @Override
  public Long getId() {
    return id;
  }
  
  @Override
  public void setId(Long newId) {
    this.id = newId;
  }
}
