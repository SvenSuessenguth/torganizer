package org.cc.torganizer.frontend.clubs.actions;

import javax.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.clubs.ClubsState;
import org.cc.torganizer.persistence.ClubsRepository;

public abstract class ClubsAction {

  @Inject
  protected ClubsState state;

  @Inject
  protected ClubsRepository clubsRepository;

  @Inject
  protected ApplicationState applicationState;
}
