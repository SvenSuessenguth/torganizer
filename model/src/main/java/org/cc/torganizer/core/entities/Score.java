package org.cc.torganizer.core.entities;

public class Score extends Entity{
  
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
