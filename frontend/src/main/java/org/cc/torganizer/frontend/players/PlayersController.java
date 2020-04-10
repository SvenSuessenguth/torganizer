package org.cc.torganizer.frontend.players;

import static org.cc.torganizer.core.entities.Status.ACTIVE;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

@Named
@RequestScoped
public class PlayersController {

  @Inject
  private Logger logger;

  @Inject
  private PlayersRepository playersRepository;

  @Inject
  private TournamentsState tournamentsState;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private ClubsRepository clubsRepository;

  @Inject
  private PlayersState playersState;

  public void save() {
    logger.info("save player");
    Long clubId = playersState.getCurrentClubId();
    Club club = clubsRepository.read(clubId);

    Player current = playersState.getCurrent();
    current.setClub(club);
    Player updated = playersRepository.update(current);
    playersState.setCurrent(updated);
  }

  public void create() {
    logger.info("create player");

    Player currentPlayer = playersState.getCurrent();
    Person currentPerson = currentPlayer.getPerson();

    Person newPerson = new Person(currentPerson.getFirstName(), currentPerson.getLastName());
    newPerson.setGender(currentPerson.getGender());
    newPerson.setDateOfBirth(currentPerson.getDateOfBirth());

    Player newPlayer = new Player(newPerson);
    newPlayer.setLastMatch(null);
    newPlayer.setClub(currentPlayer.getClub());
    newPlayer.setStatus(ACTIVE);

    playersRepository.create(newPlayer);

    Long currentTournamentsId = tournamentsState.getCurrent().getId();
    Long newPlayerId = newPlayer.getId();
    tournamentsRepository.addPlayer(currentTournamentsId, newPlayerId);

    playersState.initState();
    playersState.setCurrent(newPlayer);
  }

  /**
   * Deleting a player is not possible, if the player has completed any match
   */
  public void delete() {
    logger.info("delete player");

    // delete from tournament and from players/persons-tables
    Long tId = tournamentsState.getCurrent().getId();
    Long pId = playersState.getCurrent().getId();

    tournamentsRepository.removePlayer(tId, pId);
    playersRepository.delete(pId);
    playersState.initState();
  }

  public void select(Player player) {
    playersState.setCurrent(player);
  }
}
