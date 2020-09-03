package org.cc.torganizer.core.entities;

/**
 * Ein <i>result</i> kann das endgueltige Endergebnis eines <i>matches</i> sein,
 * oder ein Zwischenergebnis (z.B. Satzergebnis) repraesentieren.
 */
public class Result extends Entity implements Positional {

  private Integer homeScore;

  private Integer guestScore;

  /**
   * Reihenfolge der Results. -1 als Standardwert, wenn die Reihenfolge ohne
   * Bedeutung ist.
   */
  private Integer position = Integer.valueOf(-1);

  /**
   * Standardkonstruktor.
   */
  public Result() {
    position = Integer.valueOf(-1);
  }

  /**
   * Konstruktor.
   *
   * @param newPosition Index
   */
  public Result(Integer newPosition) {
    this(newPosition, Integer.valueOf(0), Integer.valueOf(0));
  }

  /**
   * Bequemlichkeitskonstruktor.
   *
   * @param newPosition   Index des Results
   * @param newHomeScore  Score des Homes
   * @param newGuestScore Score des Guests
   */
  public Result(Integer newPosition, Integer newHomeScore, Integer newGuestScore) {
    this.position = newPosition == null ? Integer.valueOf(-1) : newPosition;
    this.homeScore = newHomeScore;
    this.guestScore = newGuestScore;
  }

  /**
   * Prueft, ob beide Scores gesetzt sind.
   *
   * @return <code>true</code>, wenn beide Scores gesetzt sind, sonst
   *     <code>false</code>
   */
  public boolean areScoresSet() {
    return homeScore != null && guestScore != null;
  }

  /**
   * Das Result gilt als unentschieden, wenn home und guest den gleichen Score
   * erreicht haben.
   *
   * @return <code>true</code>, wenn das Match unentschieden ist, sonst
   *     <code>false</code>
   */
  public boolean isDraw() {
    return homeScore.equals(guestScore);
  }

  @Override
  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer newPosition) {
    this.position = newPosition;
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
