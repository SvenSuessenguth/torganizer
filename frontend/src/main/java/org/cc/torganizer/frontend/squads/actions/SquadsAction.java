package org.cc.torganizer.frontend.squads.actions;

import javax.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.squads.SquadsState;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.SquadsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

public abstract class SquadsAction {

  @Inject
  protected SquadsState state;

  @Inject
  protected ApplicationState applicationState;

  @Inject
  protected SquadsRepository squadsRepository;

  @Inject
  protected PlayersRepository playersRepository;

  @Inject
  protected TournamentsRepository tournamentsRepository;
}
