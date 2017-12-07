package org.cc.torganizer.core.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;

/**
 * Tournament, welches verwaltet werden soll.
 */
public class Tournament extends Entity{

  @JsonbProperty
  private String name;

  @JsonbTransient
  private Set<Player> subscribers = new HashSet<>();
  
  @JsonbTransient
  private Set<Opponent> opponents = new HashSet<>();

  @JsonbTransient
  private Set<Discipline> disciplines = new HashSet<>();
  
  @JsonbTransient
  private Set<Gymnasium> gymnasiums = new HashSet<>();

  /**
   * Default.
   */
  public Tournament() {
    // gem. Bean-Spec.
  }

  public String getName() {
    return name;
  }

  public void setName(String inName) {
    this.name = inName;
  }

  /**
   * Hinzufuegen einer Diszipline, die in diesem Tournament gespielt werden
   * soll.
   * 
   * @param discipline Discipline, die gespielt werden soll.
   */
  public void addDiscipline(Discipline discipline) {
    disciplines.add(discipline);
  }

  public Set<Discipline> getDisciplines() {
    return disciplines;
  }

  public void setDisciplines(Set<Discipline> disciplines) {
    this.disciplines = disciplines;
  }

  /**
   * Gibt die Liste der running Matches zur\u00fcck.
   * 
   * @return Liste der running Matches
   */
  @JsonbTransient
  public List<Match> getRunningMatches() {

    List<Match> runningMatches = new ArrayList<>();

    for (Discipline discipline : getDisciplines()) {
      Round round = discipline.getCurrentRound();
      for (Group group : round.getGroups()) {
        runningMatches.addAll(group.getRunningMatches());
      }
    }

    return runningMatches;
  }

  /**
   * Gibt eine Liste der finishedMatches zur\u00fcck. Dies umfasst nicht nur die
   * aktuellen Rounds, sondern alle Rounds.
   * 
   * @return Liste der finished Matches
   */
  @JsonbTransient
  public List<Match> getFinishedMatches() {
    List<Match> finishedMatches = new ArrayList<>();

    for (Discipline discipline : getDisciplines()) {
      for (Round round : discipline.getRounds()) {
        for (Group group : round.getGroups()) {
          for (Match match : group.getMatches()) {
            if (match.isFinished()) {
              finishedMatches.add(match);
            }
          }
        }
      }
    }

    return finishedMatches;
  }

  public Set<Player> getSubscribers() {
    return subscribers;
  }

  public void setSubscribers(Set<Player> subscribers) {
    this.subscribers = subscribers;
  }

  public Set<Opponent> getOpponents() {
    return opponents;
  }

  public void setOpponents(Set<Opponent> opponents) {
    this.opponents = opponents;
  }

  public Set<Gymnasium> getGymnasiums() {
    return gymnasiums;
  }

  public void setGymnasiums(Set<Gymnasium> gymnasiums) {
    this.gymnasiums = gymnasiums;
  }
}
