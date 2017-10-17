package org.cc.torganizer.core.entities;

import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.cc.torganizer.core.exceptions.RestrictionException;

/**
 * Auszufuehrende Disziplin innerhalb eines Turnieres (z.B. HE-A)
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Discipline")
@XmlAccessorType(XmlAccessType.FIELD)
public class Discipline
  extends AbstractBaseEntity
  implements Serializable {

  /** serialVersionUID. */
  public static final long serialVersionUID = 3321249651978154828L;

  /**
   * Darzustellender Name, wenn noch keine Disziplin zugewiesen wurde.
   */
  public static final String NULL_DISCIPLINE_NAME = "-";

  private Long id;

  private String label;
  
  @XmlTransient
  private Set<Opponent> opponents = new HashSet<>();

  @XmlTransient
  private transient List<Round> rounds = new ArrayList<Round>();
  
  @XmlTransient
  private Set<Restriction> restrictions = new HashSet<>();
  
  /**
   * in einer Discipline wird mindestens eine Round gespielt.
   */
  public Discipline() {
    Round round = new Round();
    round.setIndex(0);
    addRound(round);
  }

  /**
   * Finden der letzten Round, in der Opponents zugewiesen sind. In einer Round
   * spielen Opponents, wenn mindestens einer Group Opponents zugewisen sind.
   * 
   * @return a {@link org.cc.torganizer.core.entities.Round} object.
   */
  public Round getCurrentRound() {
    Round currentRound = null;
    for (Round r : getRounds()) {
      for (Group g : r.getGroups()) {
        if (!g.getOpponents().isEmpty()) {
          currentRound = r;
        }
      }
    }

    return currentRound;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String newLabel) {
    this.label = newLabel;
  }

  public Set<Opponent> getOpponents() {
    return opponents;
  }

  /**
   * Hinzufuegen eines Opponents zur Disziplin inklusive pruefen der
   * Restriktionen.
   * 
   * @param opponent Opponent, der der Disziplin hinzugefuegt werden soll.
   * @throws org.cc.torganizer.core.exceptions.RestrictionException wird
   *           geworfen, wenn eine Restriktion das Hinzufuegen verhindert
   */
  public void addOpponent(Opponent opponent) throws RestrictionException {
    // pruefen aller restrictions und werfen einer RestrictionException
    for (Restriction restriction : restrictions) {
      if (restriction != null && restriction.isRestricted(opponent)) {
        throw new RestrictionException("Versto\u00df gegen Restriction: " + restriction.getClass().getName());
      }
    }

    // erst hinzufuegen, wenn keine Restriction greift
    this.getOpponents().add(opponent);
  }

  /**
   * Entfernen eines Opponents aus der Disziplin.
   * 
   * @param opponent Opponent, der nicht mehr an der Disziplin teilnehmen soll.
   */
  public void removeOpponent(Opponent opponent) {
    this.getOpponents().remove(opponent);
  }

  /**
   * Hinzufuegen einer Runde, die in dieser Disziplin gespielt werden soll.
   * 
   * @param round Runde.
   */
  public void addRound(Round round) {
    round.setIndex(rounds.size());
    rounds.add(round);
  }

  public List<Round> getRounds() {
  	return unmodifiableList(rounds);
  }

  public Round getFirstRound() {
    return getRound(0);
  }

  /**
   * Lesen einer bestimmten Runde, die in der Disziplin gespielt werden soll.
   * 
   * @param index Index der gesuchten Runde.
   * @return Runde mit dem geforderten Index.
   */
  public Round getRound(int index) {
    Round round = null;
    for (Round r : getRounds()) {
      if (r.getIndex() == index) {
        round = r;
      }
    }

    return round;
  }

  public void setRounds(List<Round> newRounds) {
    this.rounds = newRounds;
  }

  
  /**
   * Liste aller Player, die aus dem Opponents erstellt wird.
   * 
   * @return Liste aller Player
   */
  public Set<Player> getPlayers() {
    Set<Player> players = new HashSet<Player>();

    for (Opponent opponent : getOpponents()) {
      players.addAll(opponent.getPlayers());
    }

    return players;
  }


  public void addRestriction(Restriction restriction) {
	restrictions.add(restriction);
  }
  
  public Set<Restriction> getRestrictions(){
	  return restrictions;
  }

  @Override
  public Long getId() {
    return id;
  }
  
  @Override
  public void setId(Long newId) {
    this.id = newId;
  }
}
