package org.cc.torganizer.frontend.clubs.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Club;

/**
 * Persisting the selected Club.
 */
@RequestScoped
@Named
public class SaveClub extends ClubsAction {

  /**
   * Saving a club.
   */
  public void execute() {
    Club current = state.getCurrent();

    if (current.getId() == null) {
      clubsRepository.create(current);
    } else {
      clubsRepository.update(current);
    }

    state.synchronize();
  }
}
