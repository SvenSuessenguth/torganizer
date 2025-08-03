package org.cc.torganizer.frontend.clubs.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Club;

@RequestScoped
@Named
public class SelectClub extends ClubsAction {

  public void execute(Club club) {
    state.setCurrent(club);
  }
}
