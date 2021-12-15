package org.cc.torganizer.core.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tournament, welches verwaltet werden soll.
 */
public class Tournament extends Entity {

  private String name = "";

  private Set<Opponent> opponents = new HashSet<>();

  private Set<Discipline> disciplines = new HashSet<>();

  private Set<Gymnasium> gymnasiums = new HashSet<>();

  public Tournament() {
  }

  public Tournament(Long id) {
    setId(id);
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
   * Returning all running matches for this tournament.
   */
  public List<Match> getRunningMatches() {
    return getDisciplines().stream()
        .flatMap(d -> d.getRounds().stream())
        .flatMap(r -> r.getGroups().stream())
        .flatMap(g -> g.getMatches().stream())
        .filter(Match::isRunning)
        .toList();
  }

  /**
   * Returning all finished matches for this tournament.
   */
  public List<Match> getFinishedMatches() {
    return getDisciplines().stream()
        .flatMap(d -> d.getRounds().stream())
        .flatMap(r -> r.getGroups().stream())
        .flatMap(g -> g.getMatches().stream())
        .filter(Match::isFinished)
        .toList();
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

  @Override
  public String toString() {
    return "[" + getId() + "] " + getName();
  }
}
