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
 * Ein Squad ist eine Zusammenfassung mehrerer Players. Dies kann z.B. im
 * Badminton ein Doppel sein.
 */
@XmlRootElement(name = "Squad")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "SQUADS")
@DiscriminatorValue("S")
@NamedQuery(name = "getAllSquads", query = "SELECT s FROM Squad s WHERE s.tournament.id = (:tournamentId)")
public class Squad extends Opponent {

  /** Ersatz f\u00fcr null-Values. */
  public static final Squad NONE = new Squad();

  @ManyToMany(targetEntity = Player.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(name = "SQUADS_PLAYERS", joinColumns = { @JoinColumn(name = "SQUAD_ID") }, inverseJoinColumns = {
      @JoinColumn(name = "PLAYER_ID") })
  private List<Player> players = new ArrayList<>();

  /**
   * Default.
   */
  public Squad() {
    // gem. Bean-Spec.
  }

  public void addPlayer(Player player) {
    players.add(player);
  }

  /** {@inheritDoc} */
  @Override
  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> newPlayers) {
    this.players = newPlayers;
  }
}
