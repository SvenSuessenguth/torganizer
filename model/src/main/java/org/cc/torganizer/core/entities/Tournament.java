package org.cc.torganizer.core.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tournament, welches verwaltet werden soll.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Tournament extends Entity {

  private String name = "";
  private Set<Opponent> opponents = new HashSet<>();
  private Set<Discipline> disciplines = new HashSet<>();
  private Set<Gymnasium> gymnasiums = new HashSet<>();

  public Tournament(Long id) {
    setId(id);
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

  /**
   * Returning all running matches for this tournament.
   */
  public List<Match> getRunningMatches() {
    return getDisciplines().stream()
      .flatMap(d -> d.getRounds().stream())
      .flatMap(r -> r.getGroups().stream())
      .flatMap(g -> g.getMatches().stream())
      .filter(Match::getRunning)
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

  @Override
  public String toString() {
    return "[" + getId() + "] " + getName();
  }
}
