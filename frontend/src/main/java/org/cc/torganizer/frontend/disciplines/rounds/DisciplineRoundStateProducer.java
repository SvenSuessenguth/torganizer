package org.cc.torganizer.frontend.disciplines.rounds;

import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;

@RequestScoped
public class DisciplineRoundStateProducer {

  @Inject
  private DisciplineRoundStateSynchronizer synchronizer;

  @Inject
  private DisciplinesCoreState disciplinesCoreState;

  @Produces
  @ConversationScoped
  @Default
  public DisciplineRoundState produces() {
    DisciplineRoundState state = new DisciplineRoundState();
    state.setDisciplinesCoreState(disciplinesCoreState);
    synchronizer.synchronize(state);

    return state;
  }
}
