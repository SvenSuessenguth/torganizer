package org.cc.torganizer.frontend.clubs.actions;

import jakarta.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.clubs.ClubsState;
import org.cc.torganizer.persistence.ClubsRepository;

/**
 * Actions for Clubs.
 */
public abstract class ClubsAction {

  @Inject
  protected ClubsState state;

  @Inject
  protected ClubsRepository clubsRepository;

  @Inject
  protected ApplicationState applicationState;
}
