package org.cc.torganizer.frontend.disciplines.rounds;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesState;
import org.cc.torganizer.persistence.TournamentsRepository;

@ConversationScoped
@Named
public class RoundsState implements Serializable, State {

  @Inject
  private ApplicationState applicationState;

  @Inject
  private DisciplinesState disciplinesState;

  @Inject
  private TournamentsRepository tournamentsRepository;

  private Round round;
  private int newGroupsCount;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {
    Discipline discipline = disciplinesState.getDiscipline();
    round = discipline.getLastRound();
  }

  public List<Round> getRounds() {
    Discipline discipline = disciplinesState.getDiscipline();
    return discipline.getRounds();
  }

  public Round getRound() {
    return round;
  }

  public void setRound(Round round) {
    this.round = round;
  }

  /**
   * Checking, if the current round is the last round.
   */
  public boolean isLastRound() {
    Discipline discipline = disciplinesState.getDiscipline();
    int highestPostion = 0;

    // getting highest round
    for (Round r : discipline.getRounds()) {
      if (r.getPosition() > highestPostion) {
        highestPostion = r.getPosition();
      }
    }

    return Objects.equals(round.getPosition(), highestPostion);
  }

  /**
   * Getting opponents that can be assigned to the current round.
   * In the first rouond all Opponents are assignable.
   * In the following rounds only the qualified of the previous round.
   */
  public Collection<Opponent> getAssignableOpponents() {
    Collection<Opponent> assignableOpponents = null;

    boolean isFirstRound = round.getPosition() == 0;
    if (isFirstRound) {
      Discipline discipline = disciplinesState.getDiscipline();
      Long tournamentId = applicationState.getTournamentId();
      assignableOpponents = tournamentsRepository
          .getAssignableOpponentsForDiscipline(tournamentId, discipline, 0, 1000);
    } else {
      assignableOpponents = round.getQualifiedOpponents();
    }

    return new HashSet<>(assignableOpponents);
  }

  public int getNewGroupsCount() {
    return newGroupsCount;
  }

  public void setNewGroupsCount(int newGroupsCount) {
    this.newGroupsCount = newGroupsCount;
  }

  public System[] getSystems() {
    return System.values();
  }

}
