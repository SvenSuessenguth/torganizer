package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;

@RequestScoped
@Named
public class AddOpponentToRoundAction extends RoundAction {
  public void execute(Opponent opponent) {
    List<Group> group = getGroupsWithMinOpponents();
    group.get(0).addOpponent(opponent);
  }

  private List<Group> getGroupsWithMinOpponents() {
    Integer minGroupsSize = Integer.MAX_VALUE;
    List<Group> groupsWithMinOppoents = new ArrayList<>();

    for (Group group : roundState.getRound().getGroups()) {
      Integer groupSize = group.getOpponents().size();
      if (groupSize < minGroupsSize) {
        minGroupsSize = groupSize;
        groupsWithMinOppoents.clear();
      }
      if (groupSize.equals(minGroupsSize)) {
        groupsWithMinOppoents.add(group);
      }
    }

    return groupsWithMinOppoents;
  }
}
