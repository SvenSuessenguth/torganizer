package org.cc.torganizer.frontend.squads;

import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

import jakarta.annotation.PostConstruct;
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
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.persistence.TournamentsRepository;

/**
 * State of the Squads-UI.
 */
@ViewScoped
@Named
public class SquadsState implements Serializable, State {

  public static final int MAX_SQUADS_RESULTS = 1000;
  public static final int MAX_PLAYERS_RESULTS = 1000;
  public static final int ALL_PLAYERS_TABLE_SIZE = 10;
  public static final int CURRENT_SQUAD_PLAYERS_TABLE_SIZE = 2;

  private Squad current;
  private List<Squad> squads;
  private List<Player> players;
  private int allPlayersTableIndex = 0;

  // Filter
  private Gender gender = UNKNOWN;

  @Inject
  private ApplicationState applicationState;
  @Inject
  private TournamentsRepository tournamentsRepository;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {
    Tournament currentTournament = applicationState.getTournament();
    Long tournamentId = currentTournament.getId();

    squads = tournamentsRepository.getSquads(tournamentId, 0, MAX_SQUADS_RESULTS);
    players = tournamentsRepository.getPlayers(tournamentId, 0, MAX_PLAYERS_RESULTS);

    // to show table, add empty players with no id, which are replaced
    // when actual players are added
    current = new Squad();
    for (int i = 0; i < CURRENT_SQUAD_PLAYERS_TABLE_SIZE; i++) {
      current.addPlayer(new Player(new Person()));
    }
  }

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
    List<Player> chunk = new ArrayList<>();
    int fromIndex = allPlayersTableIndex * ALL_PLAYERS_TABLE_SIZE;
    int toIndex = Math.min(fromIndex + ALL_PLAYERS_TABLE_SIZE, collect.size());
    chunk.addAll(collect.subList(fromIndex, toIndex));

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
