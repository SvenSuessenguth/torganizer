package org.cc.torganizer.core.entities;

import static java.util.Collections.unmodifiableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import org.cc.torganizer.core.exceptions.RestrictionException;

/**
 * Auszufuehrende Disziplin innerhalb eines Turnieres (z.B. HE-A)
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Discipline")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "DISCIPLINES")
@NamedQueries({
    @NamedQuery(name = "getDisciplinesForTournament", query = "SELECT d FROM Discipline d WHERE d.tournament.id = (:tournamentId)"),
    @NamedQuery(name = "getDisciplinesForOpponent", query = "SELECT d FROM Discipline d WHERE (:opponent) MEMBER OF d.opponents") })
public class Discipline
  extends AbstractBaseEntity
  implements Serializable {

  /** serialVersionUID. */
  public static final long serialVersionUID = 3321249651978154828L;

  /**
   * Darzustellender Name, wenn noch keine Disziplin zugewiesen wurde.
   */
  public static final String NULL_DISCIPLINE_NAME = "-";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "DISCIPLINE_ID")
  private Long id;

  @Column(name = "LABEL")
  private String label;
  
  @XmlElementRefs({ @XmlElementRef(type=Player.class), @XmlElementRef(type=Squad.class), @XmlElementRef(type=Team.class), @XmlElementRef(type=Unknown.class)}) 
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "DISCIPLINES_OPPONENTS", joinColumns = {@JoinColumn(name = "DISCIPLINE_ID") }, inverseJoinColumns = @JoinColumn(name = "OPPONENT_ID"))
  private List<Opponent> opponents = new ArrayList<Opponent>();

  @OneToMany(cascade = CascadeType.ALL)
  @OrderBy("index ASC")
  @JoinTable(name = "DISCIPLINES_ROUNDS", joinColumns = {@JoinColumn(name = "DISCIPLINE_ID") }, inverseJoinColumns = @JoinColumn(name = "ROUND_ID"))
  private List<Round> rounds = new ArrayList<Round>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "AGERESTRICTION_ID")
  private AgeRestriction ageRestriction = new AgeRestriction();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "GENDERRESTRICTION_ID")
  private GenderRestriction genderRestriction = new GenderRestriction();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "OPPONENTTYPERESTRICTION_ID")
  private OpponentTypeRestriction opponentTypeRestriction = new OpponentTypeRestriction();

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

  public List<Opponent> getOpponents() {
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
    for (Restriction restriction : getRestrictions()) {
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
   * Liste aller Restriktionen.
   * 
   * @return Liste aller Restriktionen.
   */
  public Set<Restriction> getRestrictions() {
    Set<Restriction> restrictions = new HashSet<Restriction>();
    restrictions.add(getAgeRestriction());
    restrictions.add(getGenderRestriction());
    restrictions.add(getOpponentTypeRestriction());

    return restrictions;
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

  public AgeRestriction getAgeRestriction() {
    return ageRestriction;
  }

  public void setAgeRestriction(AgeRestriction newAgeRestriction) {
    this.ageRestriction = newAgeRestriction;
  }

  public GenderRestriction getGenderRestriction() {
    return genderRestriction;
  }

  public void setGenderRestriction(GenderRestriction newGenderRestriction) {
    this.genderRestriction = newGenderRestriction;
  }

  public void setOpponents(List<Opponent> newOpponents) {
    this.opponents = newOpponents;
  }

  public OpponentTypeRestriction getOpponentTypeRestriction() {

    return opponentTypeRestriction;
  }

  public void setOpponentTypeRestriction(OpponentTypeRestriction newOpponentTypeRestriction) {
    this.opponentTypeRestriction = newOpponentTypeRestriction;
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
