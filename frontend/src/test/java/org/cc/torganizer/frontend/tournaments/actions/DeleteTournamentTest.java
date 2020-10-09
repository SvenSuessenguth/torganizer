package org.cc.torganizer.frontend.tournaments.actions;

import org.assertj.core.api.Assertions;
import org.cc.torganizer.core.entities.Tournament;
import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteTournamentTest {

  @Spy
  TournamentsState state;

  @InjectMocks
  DeleteTournament dt;

  @Test
  void testExecuteNullTournamentDoNothing() {
    dt.execute();
    Assertions.assertThat(state.getCurrent()).isNull();
  }

  @Test
  void testExecuteNotPersistedTournamentDoNothing() {
    state.setCurrent(new Tournament());
    dt.execute();
    Assertions.assertThat(state.getCurrent()).isNotNull();
  }
}