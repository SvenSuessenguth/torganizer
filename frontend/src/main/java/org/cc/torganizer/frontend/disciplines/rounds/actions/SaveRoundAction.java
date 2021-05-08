package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.persistence.DisciplinesRepository;
import org.cc.torganizer.persistence.RoundsRepository;

/**
 * Saving the current round.
 */
@RequestScoped
@Named
public class SaveRoundAction extends RoundAction {

  @Inject
  private DisciplinesRepository disciplinesRepository;

  @Inject
  private RoundsRepository roundsRepository;

  /**
   * Functional Interface method.
   */
  public void execute() {
    var round = roundState.getRound();

    int groupsSize = round.getGroups().size();
    int newGroupsCount = roundState.getNewGroupsCount();
    // creating groups, if necessary
    if (groupsSize < newGroupsCount) {
      int newGroups = newGroupsCount - groupsSize;
      for (var i = 0; i < newGroups; i++) {
        var group = new Group();
        round.appendGroup(group);
      }
    }
    // deleting (emty only) groups, if necessary
    if (groupsSize > newGroupsCount) {
      int groupsToDelete = groupsSize - newGroupsCount;
      for (var i = 0; i < groupsToDelete; i++) {
        // find deletable round and delete it
        List<Group> deletableGroups = round.getDeletableGroups();
        if (!deletableGroups.isEmpty()) {
          round.removeGroup(deletableGroups.get(0));
        }
      }
    }

    // persisting round (with optional new/deleted groups)
    if (round.getId() == null) {
      var discipline = coreState.getDiscipline();
      discipline.addRound(round);
      disciplinesRepository.update(discipline);
    } else {
      roundsRepository.update(round);
    }
  }
}
