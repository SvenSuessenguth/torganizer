package org.cc.torganizer.frontend.disciplines.core.actions;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RemoveOpponentFromDisciplineTest {

  @Mock
  private DisciplinesCoreState dcs;

  @Mock
  private DisciplinesRepository dr;

  @InjectMocks
  private RemoveOpponentFromDiscipline rofd;

  @Test
  void execute() {
    Discipline discipline = mock(Discipline.class);
    when(dcs.getDiscipline()).thenReturn(discipline);

    rofd.execute(null);
    verify(discipline, times(1)).removeOpponent(null);
    verify(dr, times(1)).update(discipline);
  }
}