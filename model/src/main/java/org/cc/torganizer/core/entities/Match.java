package org.cc.torganizer.core.entities;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Ein <i>match</i> stellt eine Spiel zwischen zwei oder mehr <i>opponents</i>
 * dar.
 */
public class Match extends Entity implements Positional {

  /**
   * Match, bei dem keiner der Opponents bekannt ist.
   */
  public static final Match NULL_MATCH;

  static {
    Opponent home = new Unknown();
    Opponent guest = new Unknown();
    NULL_MATCH = new Match(home, guest);
  }

  private Opponent home;

  private Opponent guest;

  private List<Result> results;

  /**
   * Nummer des Matches in der Gruppe, in der das Match stattfindet. Die Position
   * wird zum Beispiel f\u00fcr das System DoubleKO ben\u00f6tigt. Standardwert
   * ist '-1'.
   */
  private Integer position = -1;

  /**
   * Indikator, ob ein Match im Augnblick gespielt wird.
   */
  private Boolean running = Boolean.FALSE;

  /**
   * Wann wurde das Ergebnis eingetragen. Daraus kann die Wartezeit eines
   * Opponents seit dem letzten Spiel ermittelt werden.
   */
  private LocalDateTime finishedTime;

  /**
   * default constructor.
   */
  public Match() {
    super();
    results = new ArrayList<>();
  }

  /**
   * convinience constructor.
   *
   * @param home  home <i>opponent</i>
   * @param guest guest <i>opponent</i>
   */
  public Match(Opponent home, Opponent guest) {
    this.home = home;
    this.guest = guest;
    results = new ArrayList<>();
  }

  /**
   * Null-Safe-Variante.
   *
   * @param match Match, dessen Winner gesucht wird.
   * @return Winner oder NULL
   */
  public static Opponent getWinner(Match match) {
    if (match != null) {
      return match.getWinner();
    }

    return null;
  }

  /**
   * Der Opponent, der die meisten Results f\u00fcr sich entscheiden konnte, ist
   * Winner.
   *
   * @return Gweinner des Matches bzw. Unknown bei einem Unentschieden
   *     oder wenn das Spiel noch nicht beendet wurde.
   */
  public Opponent getWinner() {
    if (!isFinished()) {
      return new Unknown();
    }

    Opponent winner = new Unknown();
    var homeResults = 0;
    var homeScores = 0;
    var guestResults = 0;
    var guestScores = 0;

    // Aufsummieren der Results
    for (Result result : results) {
      homeScores += result.getHomeScore();
      guestScores += result.getGuestScore();

      if (result.getHomeScore() > result.getGuestScore()) {
        homeResults += 1;
      }
      if (result.getHomeScore() < result.getGuestScore()) {
        guestResults += 1;
      }
    }

    // 1. Results sind bereits eindeutig
    // oder
    // 2. Bei gleichen Results sind die Scores eindeutig
    // sonst
    // null (Unentschieden)
    if (homeResults != guestResults) {
      winner = homeResults > guestResults ? home : guest;
    } else if (homeScores != guestScores) {
      winner = homeScores > guestScores ? home : guest;
    }

    return winner;
  }

  /**
   * <p>
   * Getter for the field <code>results</code>.
   * </p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<Result> getResults() {
    return results;
  }

  /**
   * Hinzufuegen eines Results.
   *
   * @param result Result
   */
  public void addResult(Result result) {
    if (result == null) {
      throw new IllegalArgumentException("can add only non null results");
    }

    results.add(result);
  }

  /**
   * Bestimmen des Verlierers eines Matches.
   *
   * @return Verlierer des Matches oder <code>null</code>, bei unentschieden.
   */
  public Opponent getLoser() {
    Opponent winner = getWinner();
    Opponent loser;

    // Wenn es keinen Gewinner gibt, gibt es auch keinen Verlierer
    if (winner == null) {
      return null;
    }

    loser = winner.equals(home) ? guest : home;
    return loser;
  }

  public LocalDateTime getFinishedTime() {
    return finishedTime;
  }

  public void setFinishedTime(LocalDateTime finishedTime) {
    this.finishedTime = finishedTime;
  }

  public boolean isFinished() {
    return getFinishedTime() != null;
  }

  /**
   * Gibt die Opponents zurueck, die an diesem Match teilnehmen.
   *
   * @return Liste der Opponents. Zuerst home, dann guest.
   */
  public List<Opponent> getOpponents() {
    return unmodifiableList(asList(home, guest));
  }

  @Override
  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  @Override
  public String toString() {
    var result = new StringBuilder();

    Iterator<Opponent> iter = getOpponents().iterator();

    while (iter.hasNext()) {
      var opponent = iter.next();
      result.append(opponent.toString());
      if (iter.hasNext()) {
        result.append(" : ");
      }
    }

    return result.toString();
  }

  public Boolean isRunning() {
    return running;
  }

  public void setRunning(Boolean running) {
    this.running = running;
  }

  /**
   * Diese Methode wird verwendet, um festzustellen, ob ein neues Match gestartet
   * werden kann oder nicht. Wenn in dem anderen Match Players mitspielen sollen,
   * die in diesem Match bereits spielen, kann das andere Match nicht gestartet
   * werden.
   *
   * @param otherMatch Das Match, mit dem die Opponents/Players verglichen werden sollen.
   * @return <code>true</code>, wenn mindestens ein Player in beiden Matche
   *     mitspielt, sonst <code>false</code>
   */
  public boolean isSharingPlayer(Match otherMatch) {
    for (Opponent opponent : this.getOpponents()) {
      for (Player player : opponent.getPlayers()) {
        if (otherMatch.isParticipant(player)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Prueft, ob ein Player an diesem Match teilnimmt oder nicht.
   *
   * @param player Player, der geprueft werden soll.
   * @return <code>true</code>, wenn der Player an diesem Match teilnimmt, sonst
   *     <code>false</code>
   */
  protected boolean isParticipant(Player player) {
    for (Opponent o : this.getOpponents()) {
      for (Player p : o.getPlayers()) {
        if (p.equals(player)) {
          return true;
        }
      }
    }

    return false;
  }

  public Opponent getHome() {
    return home;
  }

  public void setHome(Opponent home) {
    this.home = home;
  }

  public Opponent getGuest() {
    return guest;
  }

  public void setGuest(Opponent guest) {
    this.guest = guest;
  }

  /**
   * Bestimmen der durchschnittlichen Wartezeit der Player, die an diesem Match
   * teilnehmen.
   *
   * @return Durchschnittliche Wartezeit der Player, die an dem uebergebenen Match
   *     teilnehmen
   */
  public Long getIdleTime() {

    // wenn home und guest unknown sind, dann 0
    if (home instanceof Unknown && guest instanceof Unknown) {
      return 0L;
    }

    var aggregatedIdleTime = 0L;

    for (Opponent o : getOpponents()) {
      aggregatedIdleTime += o.getIdleTime();
    }

    return aggregatedIdleTime / getOpponents().size();
  }
}
