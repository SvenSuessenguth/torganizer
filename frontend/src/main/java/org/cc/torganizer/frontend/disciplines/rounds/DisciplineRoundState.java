package org.cc.torganizer.frontend.disciplines.rounds;

import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;
import lombok.Data;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * State of the UI for editing Rounds.
 */
@Vetoed
@Data
public class DisciplineRoundState implements Serializable {

  @Inject
  private DisciplinesCoreState disciplinesCoreState;

  private Round round;
  private int newGroupsCount;
  private Long sourceGroupId;
  private Long sourceOpponentId;

  public List<Round> getRounds() {
    var discipline = disciplinesCoreState.getDiscipline();
    return discipline.getRounds();
  }

  /**
   * Checking, if the current round is the last round.
   */
  public boolean isLastRound() {
    var discipline = disciplinesCoreState.getDiscipline();
    var highestPostion = 0;

    // getting highest round
    for (var r : discipline.getRounds()) {
      if (r.getPosition() > highestPostion) {
        highestPostion = r.getPosition();
      }
    }

    return Objects.equals(round.getPosition(), highestPostion);
  }
}
