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
 * <p>
 * Score class.
 * </p>
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Score")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "SCORES")
public class Score {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "SCORE_ID")
  private Long id;

  @Column(name = "POINTS", nullable = false)
  private Integer points;

  /**
   * Bequemlichkeitskonstruktor.
   * 
   * @param newScore Score
   */
  public Score(Integer newScore) {
    this.points = newScore;
  }

  /**
   * default bean constructor.
   */
  public Score() {
    // gem. Bean-Spec.
  }

  public Integer getPoints() {
    return points;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }
}
