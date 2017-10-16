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

/**
 * Tournament, welches verwaltet werden soll.
 * 
 * @author svens
 * @version $Id: $
 */
@XmlRootElement(name = "Tournament")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tournament
  implements Serializable {

  /** serialVersionUID . */
  private static final long serialVersionUID = 3989029129624555931L;

  private Long id;

  private String name;
  
  @XmlTransient
  private Set<Player> subscribers = new HashSet<>();
  
  @XmlTransient
  private Set<Opponent> opponents = new HashSet<>();

  @XmlTransient
  private transient List<Discipline> disciplines = new ArrayList<Discipline>();

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
  
  public Long getId() {
    return this.id;
  }

  public void setId(Long newId) {
    this.id = newId;
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

  public List<Discipline> getDisciplines() {
    return unmodifiableList(disciplines);
  }

  public void setDisciplines(List<Discipline> inDisciplines) {
    this.disciplines = inDisciplines;
  }

  /**
   * Gibt die Liste der running Matches zur\u00fcck.
   * 
   * @return Liste der running Matches
   */
  public List<Match> getRunningMatches() {

    List<Match> runningMatches = new ArrayList<Match>();

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
  public List<Match> getFinishedMatches() {
    List<Match> finishedMatches = new ArrayList<Match>();

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
}
