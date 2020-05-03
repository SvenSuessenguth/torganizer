package org.cc.torganizer.frontend.squads;

import static org.cc.torganizer.core.entities.Gender.UNKNOWN;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.core.entities.Squad;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.persistence.TournamentsRepository;

@ViewScoped
@Named
public class SquadsState implements Serializable, State {

  public static final int MAX_SQUADS_RESULTS = 1000;
  public static final int MAX_PLAYERS_RESULTS = 1000;

  private Squad current;
  private List<Squad> squads;
  private List<Player> players;

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

    current = new Squad();
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

  public List<Player> getPlayers() {
    // filter by gender
    return players
        .stream()
        .filter(p -> p.getPerson().fitsGender(gender))
        .collect(Collectors.toList());
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
}
