package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.List;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Round;

/**
 * Saving the current round.
 */
@RequestScoped
@Named
public class SaveRoundAction extends RoundsAction {

  /**
   * Functional Interface method.
   */
  public void execute() {
    Round round = roundsState.getRound();

    int groupsSize = round.getGroups().size();
    int newGroupsCount = roundsState.getNewGroupsCount();
    // creating groups, if necessary
    if (groupsSize < newGroupsCount) {
      int newGroups = newGroupsCount - groupsSize;
      for (int i = 0; i < newGroups; i++) {
        Group group = new Group();
        round.appendGroup(group);
      }
    }
    // deleting (emty only) groups, if necessary
    if (groupsSize > newGroupsCount) {
      int groupsToDelete = groupsSize - newGroupsCount;
      for (int i = 0; i < groupsToDelete; i++) {
        // find deletable round and delete it
        List<Group> deletableGroups = round.getDeletableGroups();
        if (!deletableGroups.isEmpty()) {
          round.removeGroup(deletableGroups.get(0));
        }
      }
    }

    // persisting round (with optional new/deleted groups)
    if (round.getId() == null) {
      Discipline discipline = disciplinesState.getDiscipline();
      discipline.addRound(round);
      disciplinesRepository.update(discipline);
    } else {
      roundsRepository.update(round);
    }
  }
}
