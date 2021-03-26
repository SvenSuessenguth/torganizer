package org.cc.torganizer.frontend.disciplines.rounds;

import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;

/**
 * Producing a serializable and initialized state.
 */
@RequestScoped
public class DisciplineRoundStateProducer {

  @Inject
  private DisciplineRoundStateSynchronizer synchronizer;

  @Inject
  private DisciplinesCoreState disciplinesCoreState;

  /**
   * Producing a serializable and initialized state.
   */
  @Produces
  @ConversationScoped
  @Default
  @Named
  public DisciplineRoundState disciplineRound() {
    DisciplineRoundState state = new DisciplineRoundState();
    state.setDisciplinesCoreState(disciplinesCoreState);
    synchronizer.synchronize(state);

    return state;
  }
}
