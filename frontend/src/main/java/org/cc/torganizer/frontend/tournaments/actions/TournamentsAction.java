package org.cc.torganizer.frontend.tournaments.actions;

import javax.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.persistence.TournamentsRepository;

public abstract class TournamentsAction {

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  protected TournamentsState state;

  @Inject
  protected ApplicationState appState;
}
