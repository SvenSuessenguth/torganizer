package org.cc.torganizer.frontend.disciplines.core;

import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;

@RequestScoped
public class DisciplinesCoreStateProducer {

  @Inject
  private DisciplinesCoreStateSynchronizer synchronizer;

  @Produces
  @ConversationScoped
  @Default
  public DisciplinesCoreState produce() {
    Discipline discipline = new Discipline();
    discipline.addRestriction(new GenderRestriction());
    discipline.addRestriction(new OpponentTypeRestriction());
    discipline.addRestriction(new AgeRestriction());

    DisciplinesCoreState state = new DisciplinesCoreState();
    state.setDiscipline(discipline);
    synchronizer.synchronize(state);

    return state;
  }
}
