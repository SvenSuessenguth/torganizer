package org.cc.torganizer.frontend.players;

import static org.cc.torganizer.core.comparators.player.PlayerOrder.BY_LAST_UPDATE;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.comparators.player.PlayerComparator;
import org.cc.torganizer.core.comparators.player.PlayerComparatorProvider;
import org.cc.torganizer.core.comparators.player.PlayerOrder;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@Named
@ConversationScoped
public class PlayersState implements Serializable {

  public static final int MAX_PLAYERS_RESULTS = 1000;

  public static final int MAX_CLUBS_RESULTS = 1000;

  private static final long serialVersionUID = 3683970655136738688L;

  @Inject
  private transient TournamentsRepository tournamentsRepository;

  @Inject
  private transient ClubsRepository clubsRepository;

  @Inject
  private TournamentsState tournamentsState;

  @Inject
  private PlayerComparatorProvider playerComparatorProvider;

  private List<Player> players;

  private List<Club> clubs;

  private Player current;

  private Long currentClubId;

  private PlayerOrder playerOrder = BY_LAST_UPDATE;

  @PostConstruct
  public void postConstruct() {
    initState();
  }

  protected void initState() {
    Tournament currentTournament = tournamentsState.getCurrent();
    Long tournamentId = currentTournament.getId();
    players = tournamentsRepository.getPlayers(tournamentId,
        0, MAX_PLAYERS_RESULTS);

    PlayerComparator pc = playerComparatorProvider.get(playerOrder);
    Collections.sort(players, pc);

    clubs = clubsRepository.read(0, MAX_CLUBS_RESULTS);

    // pre-assignment
    if (!clubs.isEmpty()) {
      currentClubId = clubs.get(0).getId();
    }

    if (!players.isEmpty()) {
      current = players.get(0);
    } else {
      current = new Player("", "", null);
    }
  }

  public void orderPlayers() {
    PlayerComparator comparator = playerComparatorProvider.get(playerOrder);
    Collections.sort(players, comparator);
  }

  public Player getCurrent() {
    return current;
  }

  public void setCurrent(Player current) {
    this.current = current;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public List<Club> getClubs() {
    return clubs;
  }

  public List<Gender> getGenders() {
    return Arrays.asList(Gender.values());
  }

  public Long getCurrentClubId() {
    return currentClubId;
  }

  public void setCurrentClubId(Long currentClubId) {
    this.currentClubId = currentClubId;
  }

  public PlayerOrder getPlayerOrder() {
    return playerOrder;
  }

  public void setPlayerOrder(PlayerOrder playerOrder) {
    this.playerOrder = playerOrder;
  }
  public List<PlayerOrder> getPlayerOrders() {
    return Arrays.asList(PlayerOrder.values());
  }
}
