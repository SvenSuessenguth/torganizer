package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Collections;
import java.util.List;
import org.cc.torganizer.core.OpponentsToGroupsAssigner;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;

@RequestScoped
@Named
public class AddOpponentToRoundAction extends RoundAction {

  @Inject
  private OpponentsToGroupsAssigner assigner;

  public void execute(Opponent opponent) {
    Round round = roundState.getRound();
    List<Group> groups = round.getGroups();

    assigner.assign(Collections.singleton(opponent), groups);
  }
}
