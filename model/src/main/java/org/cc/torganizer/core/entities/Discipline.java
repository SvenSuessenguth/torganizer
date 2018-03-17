package org.cc.torganizer.core.entities;

import java.util.*;

import static java.util.Collections.unmodifiableList;

import org.cc.torganizer.core.exceptions.RestrictionException;

/**
 * Auszufuehrende Disziplin innerhalb eines Turnieres (z.B. HE-A)
 */
public class Discipline extends Entity {

  /**
   * Darzustellender Name, wenn noch keine Disziplin zugewiesen wurde.
   */
  public static final String NULL_DISCIPLINE_NAME = "-";

  private String name;

  private final Set<Opponent> opponents;

  private final List<Round> rounds;

  private final Set<Restriction> restrictions;

  /**
   * in einer Discipline wird mindestens eine Round gespielt.
   */
  public Discipline() {
    this.opponents = new HashSet<>();
    this.restrictions = new HashSet<>();
    this.rounds = new ArrayList<>();
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
  public final void addRound(Round round) {
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
      if (r.getPosition() == index) {
        round = r;
      }
    }

    return round;
  }

  public void setRounds(List<Round> newRounds) {
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
  public Set<Player> getPlayers() {
    Set<Player> players = new HashSet<>();

    getOpponents().forEach(opponent -> players.addAll(opponent.getPlayers()));

    return players;
  }

  public void addRestriction(Restriction restriction) {
    restrictions.add(restriction);
  }

  public Collection<Restriction> getRestrictions() {
    // for regressiontest the restrictions are ordered by discriminator
    // 1. AgeRestriction
    // 2. GenderRestriction
    // 3. OpponentTypeRestriction
    List<Restriction> orderedRestrictions = new ArrayList<>(this.restrictions);

    orderedRestrictions.sort(Comparator.comparing(o -> o.getDiscriminator().getId()));

    return orderedRestrictions;
  }
}
