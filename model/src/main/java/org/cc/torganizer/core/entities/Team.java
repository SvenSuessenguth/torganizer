package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Ein Team ist eine Zusammenfassung mehrere Opponents. im Badminton sind das
 * z.B. Squads und Players.
 */
@XmlRootElement(name = "Team")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "TEAMS")
@DiscriminatorValue("T")
@NamedQuery(name = "getAllTeams", query = "SELECT t FROM Team t WHERE t.tournament.id = (:tournamentId)")
public class Team extends Opponent {

  /** Ersatz f\u00fcr null-Values. */
  public static final Team NONE = new Team();

  /**
   * Set of actually playing opponents, which can be players but Squads or other
   * Teams also. In Badminton there are playing single players and squads
   * (double e.g.).
   */
  @ManyToMany(targetEntity = org.cc.torganizer.core.entities.Opponent.class, cascade = { CascadeType.PERSIST,
      CascadeType.MERGE })
  @JoinTable(name = "TEAMS_OPPONENTS", joinColumns = { @JoinColumn(name = "TEAM_ID") }, inverseJoinColumns = {
      @JoinColumn(name = "OPPONENT_ID") })
  private List<Opponent> opponents = new ArrayList<>();

  /**
   * Default.
   */
  public Team() {
    // gem. Bean-Spec.
  }

  public void addOpponent(Opponent opponent) {
    opponents.add(opponent);
  }

  public List<Opponent> getOpponents() {
    return opponents;
  }

  public void setOpponents(List<Opponent> newOpponents) {
    this.opponents = newOpponents;
  }

  @Override
  public List<Player> getPlayers() {
    List<Player> players = new ArrayList<>();

    for (Opponent opponent : opponents) {
      players.addAll(opponent.getPlayers());
    }

    return players;
  }
}