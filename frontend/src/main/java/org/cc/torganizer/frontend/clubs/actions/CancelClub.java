package org.cc.torganizer.frontend.clubs.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Club;

@RequestScoped
@Named
public class CancelClub extends ClubsAction {

  public String execute() {
    var club = new Club();
    state.setCurrent(club);

    return null;
  }
}
