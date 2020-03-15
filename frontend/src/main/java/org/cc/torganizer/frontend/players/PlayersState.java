package org.cc.torganizer.frontend.players;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@Named
@ConversationScoped
public class PlayersState implements Serializable {
  private static final long serialVersionUID = 3683970655136738688L;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private ClubsRepository clubsRepository;

  @Inject
  private TournamentsState tournamentsState;

  private Tournament currentTournament;

  private List<Player> players;
  private List<Club> clubs;

  private Player current;
  private Club currentClub;
  private int offset = 0;
  private int maxResults = 10;

  @PostConstruct
  public void postConstruct() {
    currentTournament = tournamentsState.getCurrent();
    players = tournamentsRepository.getPlayersOrderedByLastName(currentTournament.getId(), offset, maxResults);
    clubs = clubsRepository.read(0, 1000);

    if (!players.isEmpty()) {
      current = players.get(0);
    } else {
      current = new Player("", "", null);
    }

    if(!clubs.isEmpty()) {
      currentClub = clubs.get(0);
    }
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

  public Club getCurrentClub() {
    return currentClub;
  }

  public void setCurrentClub(Club currentClub) {
    this.currentClub = currentClub;
  }
}
