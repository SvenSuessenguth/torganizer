package org.cc.torganizer.core.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ein <i>result</i> kann das endgueltige Endergebnis eines <i>matches</i> sein,
 * oder ein Zwischenergebnis (z.B. Satzergebnis) repraesentieren.
 * 
 * @author Sven Suessenguth
 * @version $Id: $
 */
@XmlRootElement(name = "Result")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "RESULTS")
public class Result implements IIndexed {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "RESULT_ID")
  private Long id;

  @Column(name = "HOME_SCORE")
  private Integer homeScore;

  @Column(name = "GUEST_SCORE")
  private Integer guestScore;

  /**
   * Reihenfolge der Results. -1 als Standardwert, wenn die Reihenfolge ohne
   * Bedeutung ist.
   */
  @Column(name = "INDEX", nullable = false)
  private Integer index = Integer.valueOf(-1);

  /**
   * Standardkonstruktor.
   */
  public Result() {
    // gem. Bean-Spec.
  }

  /**
   * Konstruktor.
   * 
   * @param newIndex Index
   */
  public Result(Integer newIndex) {
    this(newIndex, 0, 0);
  }

  /**
   * Bequemlichkeitskonstruktor.
   * 
   * @param newIndex Index des Results
   * @param newHomeScore Score des Homes
   * @param newGuestScore Score des Guests
   */
  public Result(Integer newIndex, Integer newHomeScore, Integer newGuestScore) {
    this.index = newIndex;
    this.homeScore = newHomeScore;
    this.guestScore = newGuestScore;
  }

  /**
   * Prueft, ob beide Scores gesetzt sind.
   * 
   * @return <code>true</code>, wenn beide Scores gesetzt sind, sonst
   *         <code>false</code>
   */
  public boolean areScoresSet() {
    return homeScore != null && guestScore != null;
  }

  /**
   * Das Result gilt als unentschieden, wenn home und guest den gleichen Score
   * erreicht haben.
   * 
   * @return <code>true</code>, wenn das Match unentschieden ist, sonst
   *         <code>false</code>
   */
  public boolean isDraw() {
    return homeScore.equals(guestScore);
  }

  /** {@inheritDoc} */
  @Override
  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer inIndex) {
    this.index = inIndex;
  }

  public Integer getHomeScore() {
    return homeScore;
  }

  public void setHomeScore(Integer newHomeScore) {
    this.homeScore = newHomeScore;
  }

  public Integer getGuestScore() {
    return guestScore;
  }

  public void setGuestScore(Integer newGuestScore) {
    this.guestScore = newGuestScore;
  }
}
