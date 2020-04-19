package org.cc.torganizer.frontend.clubs.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;

/**
 * Action to create a club.
 */
@RequestScoped
@Named
public class CreateClub extends ClubsAction {

  /**
   * creating a club.
   */
  public void execute() {
    String name = state.getCurrent().getName();
    Club club = new Club(name);

    club = clubsRepository.create(club);
    state.setCurrent(club);

    applicationState.synchronize();
  }
}
