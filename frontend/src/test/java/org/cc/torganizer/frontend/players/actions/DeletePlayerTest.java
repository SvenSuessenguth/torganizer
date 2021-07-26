package org.cc.torganizer.frontend.players.actions;

import static org.mockito.Mockito.when;

import org.apache.logging.log4j.Logger;
import org.cc.torganizer.core.entities.Player;
import org.cc.torganizer.frontend.ApplicationMessages;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.players.PlayersState;
import org.cc.torganizer.frontend.players.PlayersStateSynchronizer;
import org.cc.torganizer.persistence.PlayersRepository;
import org.cc.torganizer.persistence.TournamentsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeletePlayerTest {

  @Mock
  private TournamentsRepository tRepository;

  @Mock
  private Logger logger;

  @Mock
  private ApplicationMessages applicationMessages;

  @Mock
  protected PlayersState state;

  @Mock
  protected PlayersRepository repository;

  @Mock
  protected PlayersStateSynchronizer synchronizer;

  @Mock
  protected ApplicationState applicationState;

  @InjectMocks
  private DeletePlayer deletePlayer;


  @Test
  void execute() {
    when(applicationState.getTournamentId()).thenReturn(1L);
    Player current = new Player("current", "player");
    current.setId(1L);
    when(state.getCurrent()).thenReturn(current);

    deletePlayer.execute();

    Mockito.verify(tRepository, Mockito.times(1)).removeOpponent(1L, 1L);
  }
}