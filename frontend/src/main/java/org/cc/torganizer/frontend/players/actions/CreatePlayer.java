package org.cc.torganizer.frontend.players.actions;

import static org.cc.torganizer.core.entities.Status.ACTIVE;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Club;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class CreatePlayer extends PlayersAction {

  @Inject
  private Logger logger;

  /**
   * creating a player with adding to current tournament.
   */
  public void execute() {
    logger.info("create player");

    Player currentPlayer = state.getCurrent();
    Person currentPerson = currentPlayer.getPerson();

    Person newPerson = new Person(currentPerson.getFirstName(), currentPerson.getLastName());
    newPerson.setGender(currentPerson.getGender());
    newPerson.setDateOfBirth(currentPerson.getDateOfBirth());

    Long clubId = state.getCurrentClubId();
    Club club = null;
    if (clubId != null) {
      club = clubsRepository.read(clubId);
    }

    Player newPlayer = new Player(newPerson);
    newPlayer.setLastMatch(null);
    newPlayer.setClub(club);
    newPlayer.setStatus(ACTIVE);

    playersRepository.create(newPlayer);

    Long tournamentsId = applicationState.getTournamentId();
    Long newPlayerId = newPlayer.getId();
    tournamentsRepository.addPlayer(tournamentsId, newPlayerId);

    initPlayerState.execute();
    state.setCurrent(newPlayer);
  }
}