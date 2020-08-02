package org.cc.torganizer.frontend.disciplines.core.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.frontend.ApplicationMessages;
import org.cc.torganizer.persistence.TournamentsRepository;

@RequestScoped
@Named
public class DeleteDiscipline extends DisciplinesAction {

  public static final String DISCIPLINES_I18N_BASE_NAME = "org.cc.torganizer.frontend.disciplines";

  @Inject
  private ApplicationMessages appMessages;

  @Inject
  private TournamentsRepository tournamentsRepository;


  /**
   * A club can only be deleted if it has no linked players.
   */
  public void execute() {
    Discipline discipline = state.getDiscipline();
    Long disciplineId = discipline.getId();
    Long tournamentId = applicationState.getTournamentId();

    boolean hasOpponents = !disciplinesRepository.getOpponents(disciplineId, 0, 1).isEmpty();

    if (hasOpponents) {
      appMessages.addMessage(DISCIPLINES_I18N_BASE_NAME, "no_delete_linked_players",
          new Object[]{discipline.getName()});
    } else {
      tournamentsRepository.removeDiscipline(tournamentId, disciplineId);
      disciplinesRepository.delete(disciplineId);
      state.synchronize();
    }
  }
}
