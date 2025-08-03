package org.cc.torganizer.frontend.disciplines.core;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.persistence.TournamentsRepository;

@RequestScoped
public class DisciplinesCoreStateSynchronizer {

  @Inject
  private TournamentsRepository tournamentsRepository;
  @Inject
  private ApplicationState appState;

  public void synchronize(DisciplinesCoreState state) {
    var tournamentId = appState.getTournamentId();
    state.setDisciplines(tournamentsRepository.getDisciplines(tournamentId, 0, 1000));
  }
}
