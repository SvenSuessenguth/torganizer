package org.cc.torganizer.frontend.players.actions;

import static org.cc.torganizer.core.entities.Status.ACTIVE;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;

@RequestScoped
@Named
public class CreatePlayer extends Action {

  @Inject
  private Logger logger;

  public void execute() {
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
}
