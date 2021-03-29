package org.cc.torganizer.frontend.players.actions;

import jakarta.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.players.PlayersState;
import org.cc.torganizer.frontend.players.PlayersStateSynchronizer;
import org.cc.torganizer.persistence.PlayersRepository;

/**
 * Baseclass for actions related to players.
 */
public abstract class PlayersAction {
  @Inject
  protected PlayersState state;

  @Inject
  protected PlayersRepository repository;

  @Inject
  protected PlayersStateSynchronizer synchronizer;

  @Inject
  protected ApplicationState applicationState;

}
