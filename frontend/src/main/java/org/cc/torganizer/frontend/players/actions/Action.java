package org.cc.torganizer.frontend.players.actions;

import javax.inject.Inject;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.players.PlayersService;
import org.cc.torganizer.frontend.players.PlayersState;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.cc.torganizer.persistence.ClubsRepository;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.TournamentsRepository;

public class Action {

  @Inject
  protected PlayersState playersState;

  @Inject
  protected ClubsRepository clubsRepository;

  @Inject
  protected PlayersRepository playersRepository;

  @Inject
  protected PlayersService playersService;

  @Inject
  protected ApplicationState appState;

  @Inject
  protected TournamentsRepository tournamentsRepository;

  @Inject
  protected InitPlayerState initPlayerState;
}
