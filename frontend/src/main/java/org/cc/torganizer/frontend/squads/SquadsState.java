package org.cc.torganizer.frontend.squads;

import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * State of the Squads-UI.
 */
@ViewScoped
@Named
@SuppressWarnings("unused")
public class SquadsState implements Serializable {

  public static final int ALL_PLAYERS_TABLE_SIZE = 10;

  private Squad current;
  private List<Squad> squads;
  private List<Player> players;
  private int allPlayersTableIndex = 0;

  // Filter
  private Gender gender = UNKNOWN;

  @Inject
  private ApplicationState applicationState;
  @Inject
  private transient TournamentsRepository tournamentsRepository;

  @Inject
  private SquadsStateSynchronizer synchronizer;

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
    // filter by gender
    List<Player> collect = players
        .stream()
        .filter(p -> p.getPerson().fitsGender(gender))
        .collect(Collectors.toList());

    // fill chunk of players
    int fromIndex = allPlayersTableIndex * ALL_PLAYERS_TABLE_SIZE;
    int toIndex = Math.min(fromIndex + ALL_PLAYERS_TABLE_SIZE, collect.size());
    List<Player> chunk = new ArrayList<>(collect.subList(fromIndex, toIndex));

    // fill up to 10 to show not only table headers
    int playersCount = chunk.size();
    for (int i = 0; i < ALL_PLAYERS_TABLE_SIZE - playersCount; i++) {
      chunk.add(new Player(new Person()));
    }

    return chunk;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public Gender[] getGenders() {
    return Gender.values();
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public void nextAllPlayersTableChunk() {
    allPlayersTableIndex += 1;
  }

  public void prevAllPlayersTableChunk() {
    allPlayersTableIndex -= 1;
  }

  public boolean hasPrevAllPlayersTableChunk() {
    return allPlayersTableIndex > 0;
  }

  public boolean hasNextAllPlayersTableChunk() {
    return players.size() > (allPlayersTableIndex + 1) * ALL_PLAYERS_TABLE_SIZE;
  }
}
