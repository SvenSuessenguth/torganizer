package org.cc.torganizer.frontend.disciplines.core.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.persistence.RestrictionsRepository;
import org.cc.torganizer.persistence.RoundsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@RequestScoped
@Named
public class SaveDiscipline extends DisciplinesAction {

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private RestrictionsRepository restrictionsRepository;

  @Inject
  private RoundsRepository roundsRepository;

  /**
   * Persisting a discipline.
   */
  public void execute() {
    // new restrictions should be persisted also
    Discipline discipline = state.getDiscipline();
    for (Restriction restriction : discipline.getRestrictions()) {
      if (restriction.getId() == null) {
        restrictionsRepository.create(restriction);
      } else {
        restrictionsRepository.update(restriction);
      }
    }

    if (discipline.getId() == null) {
      // saving restrictions
      disciplinesRepository.create(discipline);
      Long tournamentId = applicationState.getTournamentId();
      tournamentsRepository.addDiscipline(tournamentId, discipline);

      // every discipline must have at least one round
      Round round = new Round();
      roundsRepository.create(round);
      discipline.addRound(round);
    }

    disciplinesRepository.update(discipline);

    state.synchronize();
    state.setDiscipline(discipline);
  }
}
