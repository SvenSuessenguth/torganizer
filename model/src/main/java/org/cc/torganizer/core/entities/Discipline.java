package org.cc.torganizer.core.entities;

import static java.util.Collections.unmodifiableList;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.AGE_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.GENDER_RESTRICTION;
import static org.cc.torganizer.core.entities.Restriction.Discriminator.OPPONENT_TYPE_RESTRICTION;

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

  /**
   * Opponents that are member of the first round.
   */
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
   * Add opponent only if all restrictions are fullfilled.
   */
  public void addOpponent(Opponent opponent) {
    // pruefen aller restrictions und werfen einer RestrictionException
    for (Restriction restriction : restrictions) {
      if (restriction != null && restriction.isRestricted(opponent)) {
        throw new RestrictionException("violation with restriction "
            + restriction.getClass().getName());
      }
    }

    this.getOpponents().add(opponent);
  }

  public void removeOpponent(Opponent opponent) {
    this.getOpponents().remove(opponent);
  }

  public void addRound(Round round) {
    round.setPosition(rounds.size());
    rounds.add(round);
  }

  public void removeRound(Round round) {
    rounds.remove(round);
  }

  public List<Round> getRounds() {
    return unmodifiableList(rounds);
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
   * Getting the round with bthe given index.
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
   * Getting the players of the discipline.
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

  public GenderRestriction getGenderRestriction() {
    return (GenderRestriction) getRestriction(GENDER_RESTRICTION);
  }

  public AgeRestriction getAgeRestriction() {
    return (AgeRestriction) getRestriction(AGE_RESTRICTION);
  }

  public OpponentTypeRestriction getOpponentTypeRestriction() {
    return (OpponentTypeRestriction) getRestriction(OPPONENT_TYPE_RESTRICTION);
  }

  /**
   * Returning the last round of this discipline.
   */
  public Round getLastRound() {
    Round lastRound = null;

    for (Round round : getRounds()) {
      if (lastRound == null || lastRound.getPosition() < round.getPosition()) {
        lastRound = round;
      }
    }

    return lastRound;
  }
}