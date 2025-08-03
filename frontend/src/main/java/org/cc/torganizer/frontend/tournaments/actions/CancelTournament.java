package org.cc.torganizer.frontend.tournaments.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * Cancelling Tournamens editing.
 */
@RequestScoped
@Named
public class CancelTournament {

  @Inject
  protected TournamentsRepository tournamentsRepository;
  @Inject
  protected TournamentsState state;

  /**
   * creating a new tournament.
   */
  public String execute() {
    var newTournament = new Tournament();
    state.setCurrent(newTournament);

    return null;
  }
}
