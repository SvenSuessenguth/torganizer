package org.cc.torganizer.frontend.rounds;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.frontend.disciplines.DisciplinesState;

@ConversationScoped
@Named
public class RoundsState implements Serializable, State {

  @Inject
  private DisciplinesState disciplinesState;

  private Round round;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {
    Discipline discipline = disciplinesState.getDiscipline();
  }

  public List<Round> getRounds() {
    Discipline discipline = disciplinesState.getDiscipline();
    return discipline.getRounds();
  }



}
