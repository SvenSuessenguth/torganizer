package org.cc.torganizer.frontend.players;

import static org.cc.torganizer.core.comparators.player.PlayerOrder.BY_LAST_UPDATE;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
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
import org.cc.torganizer.frontend.utils.Chunk;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@ViewScoped
@Named
public class PlayersState implements Serializable, State {

  public static final int ALL_PLAYERS_CHUNK_SIZE = 10;

  @Inject
  private ApplicationState applicationState;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private PlayerComparatorProvider playerComparatorProvider;

  @Inject
  private ClubsRepository clubsRepository;

  private int allPlayersChunkIndex = 0;

  private List<Player> players;

  private List<Club> clubs;

  private Player current;

  private PlayerOrder playerOrder = BY_LAST_UPDATE;

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
    current = new Player(new Person());
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

  /**
   * Getting current chunk of players.
   */
  public Collection<Player> getPlayersChunk() {
    Chunk<Player> chunk = new Chunk<>();
    Collection<Player> playersChunk = chunk.get(this.players, ALL_PLAYERS_CHUNK_SIZE,
        allPlayersChunkIndex);

    // up to ALL_PLAYERS_CHUNK_SIZE
    int startIndex = playersChunk.size();
    for (int i = startIndex; i < ALL_PLAYERS_CHUNK_SIZE; i++) {
      playersChunk.add(new Player(new Person()));
    }

    return playersChunk;
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

  public Long getCurrentClubId() {
    return current.getClub() == null ? null : current.getClub().getId();
  }

  /**
   * Setting the id of the current club.
   */
  public void setCurrentClubId(Long clubId) {
    current.setClub(null);
    for (Club club : clubs) {
      if (club.getId().equals(clubId)) {
        current.setClub(club);
      }
    }
  }

  public int getAllPlayersChunkIndex() {
    return allPlayersChunkIndex;
  }

  public void setAllPlayersChunkIndex(int allPlayersChunkIndex) {
    this.allPlayersChunkIndex = allPlayersChunkIndex;
  }

  public boolean isNextAllPlayersChunkAvailable() {
    return (this.allPlayersChunkIndex + 1) * ALL_PLAYERS_CHUNK_SIZE < players.size();
  }

  public boolean isPrevAllPlayersChunkAvailable() {
    return allPlayersChunkIndex > 0;
  }
}
