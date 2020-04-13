package org.cc.torganizer.frontend.players.actions;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.comparators.player.PlayerComparator;
import org.cc.torganizer.core.comparators.player.PlayerComparatorProvider;
import org.cc.torganizer.core.comparators.player.PlayerOrder;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;

@RequestScoped
@Named
public class InitPlayerState extends PlayersAction {

  public static final int MAX_PLAYERS_RESULTS = 1000;

  public static final int MAX_CLUBS_RESULTS = 1000;

  @Inject
  private PlayerComparatorProvider playerComparatorProvider;

  public String execute(boolean refresh) {
    // prevent multiple initiallizations without any need
    if (!refresh && state.getPlayers() != null) {
      return "";
    }

    Tournament currentTournament = appState.getCurrent();
    // if no tournament is selected redirect to tournaments page
    if(currentTournament==null || currentTournament.getId()==null) {
      return "tournaments";
    }
    Long tournamentId = currentTournament.getId();

    List<Player> players = tournamentsRepository.getPlayers(tournamentId,
        0, MAX_PLAYERS_RESULTS);
    state.setPlayers(players);

    PlayerOrder playerOrder = state.getPlayerOrder();
    PlayerComparator pc = playerComparatorProvider.get(playerOrder);
    players.sort(pc);

    List<Club> clubs = clubsRepository.read(0, MAX_CLUBS_RESULTS);
    state.setClubs(clubs);

    // pre-assignment
    if (!clubs.isEmpty()) {
      Long currentClubId = clubs.get(0).getId();
      state.setCurrentClubId(currentClubId);
    }

    Player current;
    if (!players.isEmpty()) {
      current = players.get(0);
    } else {
      current = new Player("", "", null);
    }
    state.setCurrent(current);

    return "";
  }
}
