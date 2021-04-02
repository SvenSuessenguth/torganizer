package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundStateSynchronizer;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.persistence.RoundsRepository;

/**
 * Deleting the current round.
 */
@RequestScoped
@Named
public class DeleteRoundAction extends RoundAction {

  @Inject
  private DisciplinesRepository disciplinesRepository;

  @Inject
  private RoundsRepository roundsRepository;

  @Inject
  private DisciplineRoundStateSynchronizer synchronizer;

  /**
   * Execute.
   */
  public void execute() {
    // only last round in discipline with no opponents can be deleted
    // ... checks are coming
    Round round = roundState.getRound();

    Discipline discipline = coreState.getDiscipline();
    discipline.removeRound(round);
    disciplinesRepository.update(discipline);

    roundsRepository.delete(round.getId());

    synchronizer.synchronize(roundState);
  }
}
