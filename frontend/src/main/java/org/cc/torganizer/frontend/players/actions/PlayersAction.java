package org.cc.torganizer.frontend.players.actions;

import jakarta.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.players.PlayersState;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Actions for Players.
 */
public abstract class PlayersAction {

  @Inject
  protected PlayersState state;

  @Inject
  protected PlayersRepository playersRepository;

  @Inject
  protected ApplicationState applicationState;

  @Inject
  protected TournamentsRepository tournamentsRepository;
}
