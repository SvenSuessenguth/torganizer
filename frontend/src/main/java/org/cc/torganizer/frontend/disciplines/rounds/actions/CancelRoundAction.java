package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundStateSynchronizer;
import org.cc.torganizer.persistence.RoundsRepository;

@RequestScoped
@Named
public class CancelRoundAction extends RoundAction {

  @Inject
  private DisciplineRoundState state;

  @Inject
  private RoundsRepository roundsRepository;

  @Inject
  private DisciplineRoundStateSynchronizer synchronizer;

  @Override
  public void execute() {
    Round round = state.getRound();
    Long id = round.getId();
    round = roundsRepository.read(id);

    synchronizer.synchronize(state);
    state.setRound(round);
  }
}
