package org.cc.torganizer.frontend.squads;

import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

import jakarta.enterprise.inject.Vetoed;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;

/**
 * State of the Squads-UI.
 */
@Vetoed
@SuppressWarnings("unused")
public class SquadsState implements Serializable {

  public static final int ALL_PLAYERS_TABLE_SIZE = 10;

  private Squad current;
  private List<Squad> squads;
  private List<Player> players;
  private int allPlayersTableIndex = 0;

  // Filter
  private Gender gender = UNKNOWN;

  public Squad getCurrent() {
    return current;
  }

  public void setCurrent(Squad current) {
    this.current = current;
  }

  public List<Squad> getSquads() {
    return squads;
  }

  public void setSquads(List<Squad> squads) {
    this.squads = squads;
  }

  /**
   * Getting the players.
   */
  public List<Player> getPlayers() {
    if (players == null || players.isEmpty()) {
      return Collections.emptyList();
    }

    // filter by gender
    List<Player> collect = players
        .stream()
        .filter(p -> p.getPerson().fitsGender(gender))
        .toList();

    // fill chunk of players
    int fromIndex = allPlayersTableIndex * ALL_PLAYERS_TABLE_SIZE;
    int toIndex = Math.min(fromIndex + ALL_PLAYERS_TABLE_SIZE, collect.size());
    if (toIndex < fromIndex) {
      return Collections.emptyList();
    }

    List<Player> chunk = new ArrayList<>(collect.subList(fromIndex, toIndex));

    // fill up to 10 to show not only table headers
    int playersCount = chunk.size();
    for (var i = 0; i < ALL_PLAYERS_TABLE_SIZE - playersCount; i++) {
      chunk.add(new Player(new Person()));
    }

    return chunk;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
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

public boolean hasNextAllPlayersTableChunk() {
    if (players == null) {
      return false;
    }

    return players.size() > (allPlayersTableIndex + 1) * ALL_PLAYERS_TABLE_SIZE;
  }

  protected void setAllPlayersTableIndex(int allPlayersTableIndex) {
    this.allPlayersTableIndex = allPlayersTableIndex;
  }

  protected int getAllPlayersTableIndex() {
    return this.allPlayersTableIndex;
  }
}
