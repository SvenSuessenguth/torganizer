package org.cc.torganizer.frontend.rounds;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.frontend.disciplines.DisciplinesState;

@ConversationScoped
@Named
public class RoundsState implements Serializable, State {

  @Inject
  private DisciplinesState disciplinesState;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {
  }
}
