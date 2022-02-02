package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Round;
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

  @Override
  public void execute() {
    var round = roundState.getRound();
    updateNumberOfGroups(round);

    // persisting round (with optional new/deleted groups)
    if (round.getId() == null) {
      var discipline = coreState.getDiscipline();
      discipline.addRound(round);
      disciplinesRepository.update(discipline);
    } else {
      roundsRepository.update(round);
    }
  }

  private void updateNumberOfGroups(Round round) {
    int newGroupsCount = roundState.getNewGroupsCount();

    createNecessaryGroups(round, newGroupsCount);
    deleteSuperfluousGroups(round, newGroupsCount);
  }

  protected void deleteSuperfluousGroups(Round round, int newGroupsCount) {
    int currentGroupsCount = round.getGroups().size();

    if (currentGroupsCount > newGroupsCount) {
      int groupsToDelete = currentGroupsCount - newGroupsCount;
      for (var i = 0; i < groupsToDelete; i++) {
        // find deletable round and delete it
        List<Group> deletableGroups = round.getDeletableGroups();
        if (!deletableGroups.isEmpty()) {
          round.removeGroup(deletableGroups.get(0));
        }
      }
    }
  }

  protected void createNecessaryGroups(Round round, int newGroupsCount) {
    int currentGroupsCount = round.getGroups().size();
    if (currentGroupsCount < newGroupsCount) {
      int newGroups = newGroupsCount - currentGroupsCount;
      for (var i = 0; i < newGroups; i++) {
        var group = new Group();
        round.appendGroup(group);
      }
    }
  }
}
