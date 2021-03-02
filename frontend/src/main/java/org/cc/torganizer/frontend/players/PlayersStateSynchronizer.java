package org.cc.torganizer.frontend.players;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.cc.torganizer.core.comparators.player.PlayerComparator;
import org.cc.torganizer.core.comparators.player.PlayerComparatorProvider;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@RequestScoped
public class PlayersStateSynchronizer {

  @Inject
  private ApplicationState applicationState;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private PlayerComparatorProvider playerComparatorProvider;

  @Inject
  private ClubsRepository clubsRepository;

  public void synchronize(PlayersState state) {
    Tournament currentTournament = applicationState.getTournament();
    Long tournamentId = currentTournament.getId();

    List<Player> players = tournamentsRepository.getPlayers(tournamentId,
        0, 1000);

    PlayerComparator pc = playerComparatorProvider.get(state.getPlayerOrderCriteria());
    players.sort(pc);
    state.setPlayers(players);

    state.setClubs(clubsRepository.read(0, 1000));
    state.setCurrent(new Player(new Person()));
  }
}
