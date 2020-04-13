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
import org.cc.torganizer.frontend.players.PlayersState;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@RequestScoped
@Named
public class InitPlayerState extends Action {

  public static final int MAX_PLAYERS_RESULTS = 1000;

  public static final int MAX_CLUBS_RESULTS = 1000;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private ClubsRepository clubsRepository;

  @Inject
  private PlayersState playersState;

  @Inject
  private PlayerComparatorProvider playerComparatorProvider;

  public void execute(boolean refresh) {
    if (!refresh && playersState.getPlayers() != null) {
      return;
    }

    Tournament currentTournament = appState.getCurrent();
    Long tournamentId = currentTournament.getId();

    List<Player> players = tournamentsRepository.getPlayers(tournamentId,
        0, MAX_PLAYERS_RESULTS);
    playersState.setPlayers(players);

    PlayerOrder playerOrder = playersState.getPlayerOrder();
    PlayerComparator pc = playerComparatorProvider.get(playerOrder);
    players.sort(pc);

    List<Club> clubs = clubsRepository.read(0, MAX_CLUBS_RESULTS);
    playersState.setClubs(clubs);

    // pre-assignment
    if (!clubs.isEmpty()) {
      Long currentClubId = clubs.get(0).getId();
      playersState.setCurrentClubId(currentClubId);
    }

    Player current;
    if (!players.isEmpty()) {
      current = players.get(0);
    } else {
      current = new Player("", "", null);
    }
    playersState.setCurrent(current);

  }
}
