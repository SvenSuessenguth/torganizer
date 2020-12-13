package org.cc.torganizer.core.entities;

/**
 * Wrapper for points.
 */
public class Score extends Entity {

  private Integer points;

  public Score(Integer points) {
    this.points = points;
  }

  public Score() {
  }

  public Integer getPoints() {
    return points;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }
}
