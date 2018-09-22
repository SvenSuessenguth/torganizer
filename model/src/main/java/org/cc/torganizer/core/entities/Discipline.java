package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.cc.torganizer.core.exceptions.RestrictionException;

import static java.util.Collections.unmodifiableList;

/**
 * Auszufuehrende Disziplin innerhalb eines Turnieres (z.B. HE-A)
 */
public class Discipline extends Entity {

  private String name;

  private final Set<Opponent> opponents;

  private final List<Round> rounds;

  private final Set<Restriction> restrictions;

  public Discipline() {
    this.opponents = new HashSet<>();
    this.restrictions = new HashSet<>();
    this.rounds = new ArrayList<>();
  }

  /**
   * Finden der letzten Round, in der Opponents zugewiesen sind. In einer Round
   * spielen Opponents, wenn mindestens einer Group Opponents zugewisen sind.
   *
   * @return a {@link org.cc.torganizer.core.entities.Round} object.
   */
  public final Round getCurrentRound() {
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

  public final String getName() {
    return name;
  }

  public final void setName(final String newName) {
    this.name = newName;
  }

  public final Set<Opponent> getOpponents() {
    return opponents;
  }

  /**
   * Hinzufuegen eines Opponents zur Disziplin inklusive pruefen der
   * Restriktionen.
   *
   * @param opponent Opponent, der der Disziplin hinzugefuegt werden soll. *
   */
  public final void addOpponent(final Opponent opponent) {
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
   * Entfernen eines Opponents aus der Disziplin.
   *
   * @param opponent Opponent, der nicht mehr an der Disziplin teilnehmen soll.
   */
  public final void removeOpponent(final Opponent opponent) {
    this.getOpponents().remove(opponent);
  }

  /**
   * Hinzufuegen einer Runde, die in dieser Disziplin gespielt werden soll.
   *
   * @param round Runde.
   */
  public final void addRound(final Round round) {
    round.setPosition(rounds.size());
    rounds.add(round);
  }

  public final List<Round> getRounds() {
    return unmodifiableList(rounds);
  }

  public final Round getFirstRound() {
    return getRound(0);
  }

  /**
   * Lesen einer bestimmten Runde, die in der Disziplin gespielt werden soll.
   *
   * @param index Index der gesuchten Runde.
   * @return Runde mit dem geforderten Index.
   */
  public final Round getRound(final int index) {
    Round round = null;
    for (Round r : getRounds()) {
      if (r.getPosition() == index) {
        round = r;
      }
    }

    return round;
  }

  public final void setRounds(final List<Round> newRounds) {
    this.rounds.clear();

    if (newRounds != null) {
      this.rounds.addAll(newRounds);
    }
  }

  /**
   * Liste aller Player, die aus dem Opponents erstellt wird.
   *
   * @return Liste aller Player
   */
  public final Set<Player> getPlayers() {
    Set<Player> players = new HashSet<>();

    getOpponents().forEach(opponent -> players.addAll(opponent.getPlayers()));

    return players;
  }

  public final void addRestriction(final Restriction restriction) {
    restrictions.add(restriction);
  }

  public final Collection<Restriction> getRestrictions() {
    // for regressiontest the restrictions are ordered by discriminator
    // 1. AgeRestriction
    // 2. GenderRestriction
    // 3. OpponentTypeRestriction
    List<Restriction> orderedRestrictions = new ArrayList<>(this.restrictions);

    orderedRestrictions.sort(Comparator.comparing(o -> o.getDiscriminator().getId()));

    return orderedRestrictions;
  }

  public final Restriction getRestriction(final Restriction.Discriminator discriminator) {
    for (Restriction restriction : restrictions) {
      if (Objects.equals(discriminator, restriction.getDiscriminator())) {
        return restriction;
      }
    }

    return null;
  }

  public final boolean isAssignable(final Opponent opponent) {
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
