package org.cc.torganizer.frontend.disciplines.core.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Restriction;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreStateSynchronizer;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.persistence.RestrictionsRepository;
import org.cc.torganizer.persistence.RoundsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Persisting the selected Discipline.
 */
@RequestScoped
@Named
public class SaveDiscipline {

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private RestrictionsRepository restrictionsRepository;

  @Inject
  private RoundsRepository roundsRepository;

  @Inject
  private DisciplinesRepository disciplinesRepository;

  @Inject
  private ApplicationState applicationState;

  @Inject
  private DisciplinesCoreState state;

  @Inject
  private DisciplinesCoreStateSynchronizer synchronizer;

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

    synchronizer.synchronize(state);
    state.setDiscipline(discipline);
  }
}
