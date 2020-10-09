package org.cc.torganizer.frontend.tournaments.actions;

import static org.assertj.core.api.Assertions.assertThat;

import org.cc.torganizer.frontend.tournaments.TournamentsState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancelTournamentTest {

  @Spy
  TournamentsState state;

  @InjectMocks
  CancelTournament ct;

  @Test
  void testCancelSetNewTournament() {
    assertThat(state.getCurrent()).isNull();
    ct.execute();
    assertThat(state.getCurrent()).isNotNull();
  }
}