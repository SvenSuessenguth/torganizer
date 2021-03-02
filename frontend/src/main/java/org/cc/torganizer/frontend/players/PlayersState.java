package org.cc.torganizer.frontend.players;

import static org.cc.torganizer.core.comparators.player.PlayerOrderCriteria.BY_LAST_UPDATE;

import jakarta.enterprise.inject.Vetoed;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.cc.torganizer.core.comparators.player.PlayerOrderCriteria;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.utils.Chunk;

/**
 * State of the Players-UI.
 */
@Vetoed
public class PlayersState implements Serializable {

  public static final int ALL_PLAYERS_CHUNK_SIZE = 10;

  private int allPlayersChunkIndex = 0;

  private List<Player> players;

  private List<Club> clubs;

  private Player current;

  private PlayerOrderCriteria playerOrderCriteria = BY_LAST_UPDATE;

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

  public PlayerOrderCriteria getPlayerOrderCriteria() {
    return playerOrderCriteria;
  }

  public void setPlayerOrderCriteria(PlayerOrderCriteria playerOrderCriteria) {
    this.playerOrderCriteria = playerOrderCriteria;
  }

  public List<PlayerOrderCriteria> getPlayerOrderCriterias() {
    return Arrays.asList(PlayerOrderCriteria.values());
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

  public void setPlayers(List<Player> players) {
    this.players = players;
  }
}
