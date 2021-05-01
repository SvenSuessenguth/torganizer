package org.cc.torganizer.frontend.disciplines.rounds;

import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.frontend.disciplines.core.DisciplinesCoreState;

/**
 * State of the UI for editing Rounds.
 */
@Vetoed
public class DisciplineRoundState implements Serializable {

  @Inject
  private DisciplinesCoreState disciplinesCoreState;

  private Round round;
  private int newGroupsCount;
  private Long sourceGroupId;
  private Long sourceOpponentId;

  public List<Round> getRounds() {
    Discipline discipline = disciplinesCoreState.getDiscipline();
    return discipline.getRounds();
  }

  public Round getRound() {
    return round;
  }

  public void setRound(Round round) {
    this.round = round;
  }

  /**
   * Checking, if the current round is the last round.
   */
  public boolean isLastRound() {
    Discipline discipline = disciplinesCoreState.getDiscipline();
    int highestPostion = 0;

    // getting highest round
    for (Round r : discipline.getRounds()) {
      if (r.getPosition() > highestPostion) {
        highestPostion = r.getPosition();
      }
    }

    return Objects.equals(round.getPosition(), highestPostion);
  }

  public int getNewGroupsCount() {
    return newGroupsCount;
  }

  public void setNewGroupsCount(int newGroupsCount) {
    this.newGroupsCount = newGroupsCount;
  }

  public Long getSourceGroupId() {
    return sourceGroupId;
  }

  public void setSourceGroupId(Long sourceGroupId) {
    this.sourceGroupId = sourceGroupId;
  }

  public Long getSourceOpponentId() {
    return sourceOpponentId;
  }

  public void setSourceOpponentId(Long sourceOpponentId) {
    this.sourceOpponentId = sourceOpponentId;
  }

  public void setDisciplinesCoreState(DisciplinesCoreState disciplinesCoreState) {
    this.disciplinesCoreState = disciplinesCoreState;
  }
}
