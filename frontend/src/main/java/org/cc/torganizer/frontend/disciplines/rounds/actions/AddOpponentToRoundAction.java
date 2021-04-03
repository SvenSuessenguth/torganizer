package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Collections;
import java.util.List;
import org.cc.torganizer.core.OpponentToGroupsAssigner;
import org.cc.torganizer.core.OpponentToGroupsAssignerFactory;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.Round;
import org.cc.torganizer.core.entities.System;

@RequestScoped
@Named
public class AddOpponentToRoundAction extends RoundAction {

  @Inject
  private OpponentToGroupsAssignerFactory factory;

  public void execute(Opponent opponent) {
    Round round = roundState.getRound();
    List<Group> groups = round.getGroups();
    System system = round.getSystem();

    OpponentToGroupsAssigner opponentToGroupsAssigner = factory.getOpponentToGroupsAssigner(system);
    opponentToGroupsAssigner.assign(Collections.singleton(opponent), groups);
  }
}
