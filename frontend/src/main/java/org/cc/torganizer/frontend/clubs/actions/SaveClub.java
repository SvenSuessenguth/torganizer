package org.cc.torganizer.frontend.clubs.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.clubs.ClubsStateSynchronizer;

@RequestScoped
@Named
public class SaveClub extends ClubsAction {

  @Inject
  private ClubsStateSynchronizer synchronizer;

  public void execute() {
    var current = state.getCurrent();

    if (current.getId() == null) {
      clubsRepository.create(current);
    } else {
      clubsRepository.update(current);
    }

    synchronizer.synchronize(state);
  }
}
