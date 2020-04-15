package org.cc.torganizer.frontend.squads.actions;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;

@RequestScoped
@Named
public class InitSquadsState extends SquadsAction {

  public static final int MAX_SQUADS_RESULTS = 1000;
  public static final int MAX_PLAYERS_RESULTS = 1000;

  public String execute(boolean refresh) {
    // prevent multiple initiallizations without any need
    if (!refresh && state.getSquads() != null) {
      return null;
    }

    Tournament currentTournament = applicationState.getCurrent();
    // if no tournament is selected redirect to tournaments page
    if (currentTournament == null || currentTournament.getId() == null) {
      return "tournaments";
    }
    Long tournamentId = currentTournament.getId();

    List<Squad> squads = tournamentsRepository.getSquads(tournamentId, 0, MAX_SQUADS_RESULTS);
    state.setSquads(squads);
    List<Player> players = tournamentsRepository.getPlayers(tournamentId, 0, MAX_PLAYERS_RESULTS);
    state.setPlayers(players);

    Squad current;
    if (!squads.isEmpty()) {
      current = squads.get(0);
    } else {
      current = new Squad();
    }
    state.setCurrent(current);

    return null;
  }
}
