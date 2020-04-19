package org.cc.torganizer.frontend.players;

import static org.cc.torganizer.core.comparators.player.PlayerOrder.BY_LAST_UPDATE;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.comparators.player.PlayerComparator;
import org.cc.torganizer.core.comparators.player.PlayerComparatorProvider;
import org.cc.torganizer.core.comparators.player.PlayerOrder;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@Named
@ViewScoped
public class PlayersState extends State implements Serializable {

  private static final long serialVersionUID = 3683970655136738688L;

  @Inject
  private ApplicationState applicationState;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private PlayerComparatorProvider playerComparatorProvider;

  @Inject
  private ClubsRepository clubsRepository;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {

    Tournament currentTournament = applicationState.getTournament();
    Long tournamentId = currentTournament.getId();

    players = tournamentsRepository.getPlayers(tournamentId,
        0, 1000);

    PlayerComparator pc = playerComparatorProvider.get(playerOrder);
    players.sort(pc);

    clubs = clubsRepository.read(0, 1000);

    Club club = clubs.get(0);
    current = new Player(new Person());
    current.setClub(club);
  }

  private List<Player> players;

  private List<Club> clubs;

  private Player current;

  private PlayerOrder playerOrder = BY_LAST_UPDATE;

  public Player getCurrent() {
    return current;
  }

  public void setCurrent(Player current) {
    this.current = current;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public List<Club> getClubs() {
    return clubs;
  }

  public void setClubs(List<Club> clubs) {
    this.clubs = clubs;
  }

  public List<Gender> getGenders() {
    return Arrays.asList(Gender.values());
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

  public void setCurrentClubId(Long clubId) {
    for (Club club : clubs) {
      if (club.getId().equals(clubId)) {
        current.setClub(club);
      }
    }
  }

  public Long getCurrentClubId() {
    return current.getClub().getId();
  }
}
