package org.cc.torganizer.frontend.disciplines.rounds;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DisciplineRoundStateTest {

  @Mock
  private DisciplinesCoreState coreState;

  @InjectMocks
  private DisciplineRoundState roundState;

  @Test
  void isLastRound_true() {
    Round currentRound = new Round();
    roundState.setRound(currentRound);

    Discipline discipline = new Discipline();
    discipline.addRound(new Round());
    discipline.addRound(new Round());
    discipline.addRound(currentRound);
    when(coreState.getDiscipline()).thenReturn(discipline);

    boolean isLastRound = roundState.isLastRound();

    assertThat(isLastRound).isTrue();
  }

  @Test
  void isLastRound_false() {
    Round currentRound = new Round();
    roundState.setRound(currentRound);

    Discipline discipline = new Discipline();
    discipline.addRound(new Round());
    discipline.addRound(currentRound);
    discipline.addRound(new Round());
    when(coreState.getDiscipline()).thenReturn(discipline);

    boolean isLastRound = roundState.isLastRound();

    assertThat(isLastRound).isFalse();
  }

}