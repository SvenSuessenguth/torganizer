package org.cc.torganizer.frontend.clubs.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.clubs.ClubsStateSynchronizer;

@RequestScoped
@Named
public class SynchronizeClubsState extends ClubsAction {

  @Inject
  private ClubsStateSynchronizer synchronizer;

  public void execute() {
    synchronizer.synchronize(state);
  }
}
