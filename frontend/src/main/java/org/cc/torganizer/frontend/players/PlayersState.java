package org.cc.torganizer.frontend.players;

import jakarta.enterprise.inject.Vetoed;
import lombok.Data;
import org.cc.torganizer.core.comparators.player.PlayerOrderCriteria;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.utils.Chunk;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.cc.torganizer.core.comparators.player.PlayerOrderCriteria.BY_LAST_UPDATE;

/**
 * State of the Players-UI.
 */
@Vetoed
@Data
public class PlayersState implements Serializable {

  public static final int ALL_PLAYERS_CHUNK_SIZE = 10;

  private int allPlayersChunkIndex = 0;
  private List<Player> players;
  private List<Club> clubs;
  private Player current;
  private PlayerOrderCriteria playerOrderCriteria = BY_LAST_UPDATE;

  /**
   * Getting current chunk of players.
   */
  public Collection<Player> getPlayersChunk() {
    var chunk = new Chunk<Player>();
    var playersChunk = chunk.get(this.players, ALL_PLAYERS_CHUNK_SIZE, allPlayersChunkIndex);

    // up to ALL_PLAYERS_CHUNK_SIZE with empty players
    // to have an even view
    var startIndex = playersChunk.size();
    for (var i = startIndex; i < ALL_PLAYERS_CHUNK_SIZE; i++) {
      playersChunk.add(new Player(new Person()));
    }

    return playersChunk;
  }

  public List<Gender> getGenders() {
    return Arrays.asList(Gender.values());
  }

  public List<PlayerOrderCriteria> getPlayerOrderCriterias() {
    return Arrays.asList(PlayerOrderCriteria.values());
  }

  public Long getCurrentPlayersClubById() {
    return current.getClub() == null ? null : current.getClub().getId();
  }

  /**
   * Setting the current club.
   */
  public void setCurrentPlayersClubById(Long clubId) {
    current.setClub(null);
    for (var club : clubs) {
      if (club.getId().equals(clubId)) {
        current.setClub(club);
      }
    }
  }

  public boolean isNextAllPlayersChunkAvailable() {
    return (this.allPlayersChunkIndex + 1) * ALL_PLAYERS_CHUNK_SIZE < players.size();
  }

  public boolean isPrevAllPlayersChunkAvailable() {
    return allPlayersChunkIndex > 0;
  }
}
