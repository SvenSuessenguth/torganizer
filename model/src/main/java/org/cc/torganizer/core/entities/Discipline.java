package org.cc.torganizer.core.entities;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.cc.torganizer.core.exceptions.RestrictionException;

/**
 * Auszufuehrende Disziplin innerhalb eines Turnieres (z.B. HE-A)
 */
public class Discipline extends Entity {

  private String name;

  private Set<Opponent> opponents = new HashSet<>();

  private List<Round> rounds = new ArrayList<>();

  private Set<Restriction> restrictions = new HashSet<>();

  /**
   * Finden der letzten Round, in der Opponents zugewiesen sind. In einer Round
   * spielen Opponents, wenn mindestens einer Group Opponents zugewisen sind.
   *
   * @return a {@link org.cc.torganizer.core.entities.Round} object.
   */
  Round getCurrentRound() {
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

  public String getName() {
    return name;
  }

  public void setName(String newName) {
    this.name = newName;
  }

  public Set<Opponent> getOpponents() {
    return opponents;
  }

  /**
   * Hinzufuegen eines Opponents zur Disziplin inklusive pruefen der
   * Restriktionen.
   *
   * @param opponent Opponent, der der Disziplin hinzugefuegt werden soll. *
   */
  public void addOpponent(Opponent opponent) {
    // pruefen aller restrictions und werfen einer RestrictionException
    for (Restriction restriction : restrictions) {
      if (restriction != null && restriction.isRestricted(opponent)) {
        throw new RestrictionException("violation with restriction "
            + restriction.getClass().getName());
      }
    }

    // erst hinzufuegen, wenn keine Restriction greift
    this.getOpponents().add(opponent);
  }

  /**
   * Hinzufuegen einer Runde, die in dieser Disziplin gespielt werden soll.
   *
   * @param round Runde.
   */
  public void addRound(Round round) {
    round.setPosition(rounds.size());
    rounds.add(round);
  }

  public List<Round> getRounds() {
    return unmodifiableList(rounds);
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
      if (r.getPosition() == index) {
        round = r;
      }
    }

    return round;
  }

  /**
   * Replace current rounds with new rounds.
   */
  public void setRounds(List<Round> rounds) {
    this.rounds.clear();

    if (rounds != null) {
      this.rounds.addAll(rounds);
    }
  }

  /**
   * Liste aller Player, die aus dem Opponents erstellt wird.
   *
   * @return Liste aller Player
   */
  public Set<Player> getPlayers() {
    Set<Player> players = new HashSet<>();

    getOpponents().forEach(opponent -> players.addAll(opponent.getPlayers()));

    return players;
  }

  public void addRestriction(Restriction restriction) {
    restrictions.add(restriction);
  }

  /**
   * The restrictions are ordered by discriminator.
   * <ol>
   * <li>AgeRestriction</li>
   * <li>GenderRestriction</li>
   * <li>OpponentTypeRestriction</li>
   * </ol>
   */
  public Collection<Restriction> getRestrictions() {

    List<Restriction> orderedRestrictions = new ArrayList<>(this.restrictions);

    orderedRestrictions.sort(Comparator.comparing(o -> o.getDiscriminator().getId()));

    return orderedRestrictions;
  }

  /**
   * Returns the Rstrictions with the given discriminator.
   */
  public Restriction getRestriction(Restriction.Discriminator discriminator) {
    for (Restriction restriction : restrictions) {
      if (Objects.equals(discriminator, restriction.getDiscriminator())) {
        return restriction;
      }
    }

    return null;
  }

  /**
   * Checks, if the opponent is assignable and corrosponds to all restrictions.
   */
  public boolean isAssignable(Opponent opponent) {
    boolean assignable = true;

    // restricted by restrition
    for (Restriction restriction : restrictions) {
      if (restriction.isRestricted(opponent)) {
        assignable = false;
        break;
      }
    }

    // already in discipline
    if (opponents.contains(opponent)) {
      assignable = false;
    }

    return assignable;
  }
}