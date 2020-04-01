package org.cc.torganizer.frontend.players;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
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

  public static final int MAX_PLAYERS_RESULTS = 50;

  private static final long serialVersionUID = 3683970655136738688L;

  @Inject
  private transient TournamentsRepository tournamentsRepository;

  @Inject
  private transient ClubsRepository clubsRepository;

  @Inject
  private TournamentsState tournamentsState;

  private List<Player> players;
  private List<Club> clubs;

  private Player current;

  private Long currentClubId;

  private int offset = 0;

  @PostConstruct
  public void postConstruct() {
    initState();
  }

  protected void initState() {
    Tournament currentTournament = tournamentsState.getCurrent();
    Long tournamentId = currentTournament.getId();
    players = tournamentsRepository.getPlayersOrderedByLastName(tournamentId,
        offset, MAX_PLAYERS_RESULTS);
    clubs = clubsRepository.read(0, 1000);

    // pre-assignment
    if(!clubs.isEmpty()){
      currentClubId = clubs.get(0).getId();
    }

    if (!players.isEmpty()) {
      current = players.get(0);
    } else {
      current = new Player("", "", null);
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

  public List<Gender> getGenders() {
    return Arrays.asList(Gender.values());
  }

  public Long getCurrentClubId() {
    return currentClubId;
  }

  public void setCurrentClubId(Long currentClubId) {
    this.currentClubId = currentClubId;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }
}
