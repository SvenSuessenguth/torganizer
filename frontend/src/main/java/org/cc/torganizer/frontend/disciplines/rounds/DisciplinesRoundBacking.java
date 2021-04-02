package org.cc.torganizer.frontend.disciplines.rounds;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Collection;
import java.util.HashSet;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Accessing data from outside the state or the state itself.
 */
@RequestScoped
@Named
public class DisciplinesRoundBacking {

  @Inject
  private ApplicationState appState;

  @Inject
  private DisciplinesCoreState disciplinesCoreState;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private DisciplineRoundState state;

  public System[] getSystems() {
    return System.values();
  }

  /**
   * Getting opponents that can be assigned to the current round.
   * In the first rouond all Opponents are assignable.
   * In the following rounds only the qualified of the previous round.
   */
  public Collection<Opponent> getAssignableOpponents() {
    Collection<Opponent> assignableOpponents;

    Round currentRound = state.getRound();
    boolean isFirstRound = currentRound.getPosition() == 0;
    if (isFirstRound) {
      Discipline discipline = disciplinesCoreState.getDiscipline();
      Long tournamentId = appState.getTournamentId();
      assignableOpponents = tournamentsRepository
          .getAssignableOpponentsForDiscipline(tournamentId, discipline, 0, 1000);
    } else {
      assignableOpponents = currentRound.getQualifiedOpponents();
    }

    return new HashSet<>(assignableOpponents);
  }
}
