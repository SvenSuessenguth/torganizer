package org.cc.torganizer.frontend.disciplines.rounds;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.cc.torganizer.core.comparators.OpponentByNameComparator;
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

    var currentRound = state.getRound();
    boolean isFirstRound = currentRound.getPosition() == 0;
    if (isFirstRound) {
      var discipline = disciplinesCoreState.getDiscipline();
      var tournamentId = appState.getTournamentId();
      assignableOpponents = tournamentsRepository
          .getAssignableOpponentsForDiscipline(tournamentId, discipline, 0, 1000);
    } else {
      assignableOpponents = currentRound.getQualifiedOpponents();
    }

    removeAlreadyAssignedOpponents(assignableOpponents, currentRound);
    return sortByName(assignableOpponents);
  }

  protected void removeAlreadyAssignedOpponents(Collection<Opponent> assignableOpponents,
                                                Round currentRound) {
    var alreadyAssignedOpponents = new ArrayList<Opponent>();
    for (var group : currentRound.getGroups()) {
      alreadyAssignedOpponents.addAll(group.getOpponents());
    }

    assignableOpponents.removeAll(alreadyAssignedOpponents);
  }

  protected List<Opponent> sortByName(Collection<Opponent> assignableOpponents) {
    var opponents = new ArrayList<>(assignableOpponents);
    opponents.sort(new OpponentByNameComparator());

    return opponents;
  }
}
