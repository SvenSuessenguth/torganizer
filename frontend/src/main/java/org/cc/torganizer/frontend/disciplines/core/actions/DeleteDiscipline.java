package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.ApplicationMessages;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreStateSynchronizer;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@RequestScoped
@Named
public class DeleteDiscipline {

  public static final String DISCIPLINES_I18N_BASE_NAME = "org.cc.torganizer.frontend.disciplines";

  @Inject
  private ApplicationMessages appMessages;

  @Inject
  private ApplicationState applicationState;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private DisciplinesRepository disciplinesRepository;

  @Inject
  private DisciplinesCoreState state;

  @Inject
  private DisciplinesCoreStateSynchronizer synchronizer;

  /**
   * A club can only be deleted if it has no linked players.
   */
  public void execute() {
    var discipline = state.getDiscipline();
    var disciplineId = discipline.getId();
    var tournamentId = applicationState.getTournamentId();

    var hasOpponents = !disciplinesRepository.getOpponents(disciplineId, 0, 1).isEmpty();

    if (hasOpponents) {
      appMessages.addMessage(DISCIPLINES_I18N_BASE_NAME, "no_delete_linked_players",
          new Object[]{discipline.getName()});
    } else {
      tournamentsRepository.removeDiscipline(tournamentId, disciplineId);
      disciplinesRepository.delete(disciplineId);
      synchronizer.synchronize(state);
    }
  }
}
