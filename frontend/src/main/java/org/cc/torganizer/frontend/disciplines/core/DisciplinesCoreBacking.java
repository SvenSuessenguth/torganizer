package org.cc.torganizer.frontend.disciplines.core;

import static org.cc.torganizer.core.entities.OpponentType.PLAYER;
import static org.cc.torganizer.core.entities.OpponentType.SQUAD;
import static org.cc.torganizer.core.entities.OpponentType.TEAM;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Arrays;
import java.util.List;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.OpponentType;

/**
 * Access data for core disciplines outside the state.
 */
@RequestScoped
@Named
public class DisciplinesCoreBacking {

  @Inject
  private DisciplinesCoreState disciplinesCoreState;

  public List<OpponentType> getOpponentTypes() {
    return Arrays.asList(PLAYER, SQUAD, TEAM);
  }

  public Gender[] getGenders() {
    return Gender.values();
  }

  public DisciplinesCoreState getState() {
    return disciplinesCoreState;
  }
}
