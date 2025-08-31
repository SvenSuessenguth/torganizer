package org.cc.torganizer.frontend.squads;

import jakarta.enterprise.inject.Vetoed;
import lombok.Data;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

/**
 * State of the Squads-UI.
 */
@Vetoed
@Data
public class SquadsState implements Serializable {

  public static final int ALL_PLAYERS_TABLE_SIZE = 10;

  private Squad current;
  private List<Squad> squads;
  private List<Player> players;
  private int allPlayersTableIndex = 0;

  // Filter
  private Gender gender = UNKNOWN;

  /**
   * Getting the players.
   */
  public List<Player> getPlayers() {
    if (players == null || players.isEmpty()) {
      return Collections.emptyList();
    }

    // filter by gender
    var collect = players
      .stream()
      .filter(p -> p.getPerson().fitsGender(gender))
      .toList();

    // fill chunk of players
    var fromIndex = allPlayersTableIndex * ALL_PLAYERS_TABLE_SIZE;
    var toIndex = Math.min(fromIndex + ALL_PLAYERS_TABLE_SIZE, collect.size());
    if (toIndex < fromIndex) {
      return Collections.emptyList();
    }

    var chunk = new ArrayList<>(collect.subList(fromIndex, toIndex));

    // fill up to 10 to show not only table headers
    var playersCount = chunk.size();
    for (var i = 0; i < ALL_PLAYERS_TABLE_SIZE - playersCount; i++) {
      chunk.add(new Player(new Person()));
    }

    return chunk;
  }

  public void incAllPlayersTableChunk() {
    allPlayersTableIndex += 1;
  }

  /**
   * Decreasing the index of the chunk of currently displayed players.
   */
  public void decAllPlayersTableChunk() {
    if (allPlayersTableIndex > 0) {
      allPlayersTableIndex -= 1;
    } else {
      allPlayersTableIndex = 0;
    }
  }

  public boolean hasPrevAllPlayersTableChunk() {
    return allPlayersTableIndex > 0;
  }

  /**
   * Is there a next chunk with players to display.
   */
  public boolean hasNextAllPlayersTableChunk() {
    if (players == null) {
      return false;
    }

    return players.size() > (allPlayersTableIndex + 1) * ALL_PLAYERS_TABLE_SIZE;
  }
}
