package org.cc.torganizer.frontend.disciplines.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.persistence.TournamentsRepository;

@RequestScoped
@Named
public class SaveDiscipline extends DisciplinesAction {

  @Inject
  private TournamentsRepository tournamentsRepository;

  public void execute() {
    Discipline discipline = state.getDiscipline();

    if (discipline.getId() == null) {
      // saving restrictions


      disciplinesRepository.create(discipline);
    } else {
      disciplinesRepository.update(discipline);
    }

    Long tournamentId = applicationState.getTournamentId();
    tournamentsRepository.addDiscipline(tournamentId, discipline);

    state.synchronize();
    applicationState.synchronize();
  }
}
