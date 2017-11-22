package org.cc.torganizer.core.entities;

import static java.util.Collections.unmodifiableList;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ein <i>match</i> stellt eine Spiel zwischen zwei oder mehr <i>opponents</i>
 * dar.
 * 
 * @author svens $LastChangedBy: u000349 $<br />
 *         $LastChangedDate: 2017-10-16 08:40:18 +0200 (Mo, 16 Okt 2017) $<br />
 *         $Revision: 1368 $
 * @version $Id: $
 */
@XmlRootElement(name = "Match")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "MATCHES")
@NamedQueries({ @NamedQuery(name = "getMatchesForGroup", query = "SELECT m FROM Match m WHERE m.group = :group"),
    @NamedQuery(name = "getRunningMatchesForGroup", query = "SELECT m FROM Match m WHERE m.running = true AND m.group = :group"),
    @NamedQuery(name = "getFinishedMatchesForGroup", query = "SELECT m FROM Match m WHERE m.running = true AND m.finished IS NOT NULL AND m.group = :group") })
public class Match implements IPositional {

  /**
   * Match, bei dem keiner der Opponents bekannt ist.
   */
  public static final Match NULL_MATCH;
  static {
    Opponent home = new Unknown();
    Opponent guest = new Unknown();
    NULL_MATCH = new Match(home, guest);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "MATCH_ID")
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "HOME_ID", nullable = true)
  private Opponent home;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "GUEST_ID", nullable = true)
  private Opponent guest;

  @OneToMany(cascade = CascadeType.ALL)
  @OrderBy("index ASC")
  @JoinTable(name = "MATCHES_RESULTS", joinColumns = {
      @JoinColumn(name = "MATCH_ID") }, inverseJoinColumns = @JoinColumn(name = "RESULT_ID"))
  private List<Result> results = new ArrayList<>();

  
  /**
   * Nummer des Matches in der Gruppe, in der das Match stattfindet. Der Index
   * wird zum Beispiel f\u00fcr das System DoubleKO ben\u00f6tigt. Standardwert
   * ist '-1'.
   */
  @Column(name = "INDEX", nullable = false)
  private Integer index = Integer.valueOf(-1);

  /**
   * Indikator, ob ein Match im Augnblick gespielt wird.
   */
  @Column(name = "RUNNING", nullable = false)
  private Boolean running = Boolean.FALSE;

  /**
   * Wann wurde das Ergebnis eingetragen. Daraus kann die Wartezeit eines
   * Opponents seit dem letzten Spiel ermittelt werden.
   */
  @Column(name = "FINISHED", nullable = true)
  private LocalDateTime finishedTime;

  /**
   * default constructor.
   */
  public Match() {
    super();
  }

  /**
   * convinience constructor.
   * 
   * @param newHome
   *          home <i>opponent</i>
   * @param newGuest
   *          guest <i>opponent</i>
   */
  public Match(Opponent newHome, Opponent newGuest) {
    this.home = newHome;
    this.guest = newGuest;
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
   * @param result
   *          Result
   */
  public void addResult(Result result) {
    assert result != null;

    results.add(result);
  }

  /**
   * Null-Safe-Variante.
   * 
   * @param match
   *          Match, dessen Winner gesucht wird.
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
   *         oder wenn das Spiel noch nicht beendet wurde.
   */
  public Opponent getWinner() {
    if (!isFinished()) {
      return new Unknown();
    }

    Opponent winner = new Unknown();
    int homeResults = 0;
    int homeScores = 0;
    int guestResults = 0;
    int guestScores = 0;

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
   * Bestimmen des Verlierers eines Matches.
   * 
   * @return Verlierer des Matches oder <code>null</code>, bei unentschieden.
   */
  public Opponent getLoser() {
    Opponent winner = getWinner();
    Opponent loser = null;

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

  public boolean isFinished() {
    return getFinishedTime() != null;
  }

  public void setFinishedTime(LocalDateTime newFinishedTime) {
    this.finishedTime = newFinishedTime;
  }

  /**
   * Gibt die Opponents zurueck, die an diesem Match teilnehmen.
   * 
   * @return Liste der Opponents. Zuerst home, dann guest.
   */
  public List<Opponent> getOpponents() {
    List<Opponent> opponents = new ArrayList<>();

    opponents.add(home);
    opponents.add(guest);

    return unmodifiableList(opponents);
  }

  /** {@inheritDoc} */
  @Override
  public Integer getPosition() {
    return index;
  }

  public void setIndex(Integer newIndex) {
    this.index = newIndex;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    Iterator<Opponent> iter = getOpponents().iterator();

    while (iter.hasNext()) {
      Opponent opponent = iter.next();
      result.append(opponent.toString());
      if (iter.hasNext()) {
        result.append(" : ");
      }
    }

    return result.toString();
  }

  public Long getId() {
    return this.id;
  }

  public Boolean isRunning() {
    return running;
  }

  public void setRunning(Boolean pRunning) {
    this.running = pRunning;
  }

  /**
   * Diese Methode wird verwendet, um festzustellen, ob ein neues Match gestartet
   * werden kann oder nicht. Wenn in dem anderen Match Players mitspielen sollen,
   * die in diesem Match bereits spielen, kann das andere Match nicht gestartet
   * werden.
   * 
   * @param otherMatch
   *          Das Match, mit dem die Opponents/Players verglichen werden sollen.
   * @return <code>true</code>, wenn mindestens ein Player in beiden Matche
   *         mitspielt, sonst <code>false</code>
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
   * @param player
   *          Player, der geprueft werden soll.
   * @return <code>true</code>, wenn der Player an diesem Match teilnimmt, sonst
   *         <code>false</code>
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

  public void setHome(Opponent newHome) {
    this.home = newHome;
  }

  public Opponent getGuest() {
    return guest;
  }

  public void setGuest(Opponent newGuest) {
    this.guest = newGuest;
  }

  /**
   * Bestimmen der durchschnittlichen Wartezeit der Player, die an diesem Match
   * teilnehmen.
   * 
   * @return Durchschnittliche Wartezeit der Player, die an dem uebergebenen Match
   *         teilnehmen
   */
  public Long getIdleTime() {

    // wenn home und guest unknown sind, dann 0
    if (home instanceof Unknown && guest instanceof Unknown) {
      return 0L;
    }

    LocalDateTime nullDate = LocalDateTime.MIN;
    LocalDateTime now = LocalDateTime.now();
    Long overallTimeSinceLastMatch = 0L;
    Integer playerCount = 0;

    List<Opponent> opponents = getOpponents();
    for (Opponent o : opponents) {
      for (Player p : o.getPlayers()) {
        playerCount += 1;
        LocalDateTime lastMatch = p.getLastMatch();
        lastMatch = lastMatch == null ? nullDate : lastMatch;
        overallTimeSinceLastMatch += ChronoUnit.MINUTES.between(now, lastMatch);
      }
    }

    return playerCount!=0?overallTimeSinceLastMatch / playerCount:Long.MAX_VALUE;
  }
}
