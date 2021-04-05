package org.cc.torganizer.frontend.disciplines.rounds;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;

/**
 * Synchronizing the disciplines round  state with db-data.
 */
@RequestScoped
public class DisciplineRoundStateSynchronizer {

  @Inject
  private DisciplinesCoreState coreState;

  /**
   * Synchronizing the disciplines round  state with db-data.
   */
  public void synchronize(DisciplineRoundState state) {
    Discipline discipline = coreState.getDiscipline();

    Round lastRound = discipline.getLastRound();
    state.setRound(lastRound);
    state.setNewGroupsCount(lastRound.getGroups().size());
  }
}