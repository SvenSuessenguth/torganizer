package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.cc.torganizer.core.OpponentsToGroupsAssigner;
import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundStateSynchronizer;
import org.cc.torganizer.persistence.RoundsRepository;

/**
 * Adding an opponent to the groups of the round.
 */
@RequestScoped
@Named
public class AddOpponentToRoundAction {

  @Inject
  private DisciplineRoundStateSynchronizer synchronizer;

  @Inject
  private OpponentsToGroupsAssigner assigner;

  @Inject
  private RoundsRepository roundsRepository;

  @Inject
  protected DisciplineRoundState roundState;

  public void execute(Opponent opponent) {
    var round = roundState.getRound();
    List<Group> groups = round.getGroups();

    Set<Opponent> opponents = new HashSet<>();
    opponents.add(opponent);
    assigner.assign(opponents, groups);

    roundsRepository.update(round);
    synchronizer.synchronize(roundState);

    // reset to previous selected round
    roundState.setRound(round);
  }
}
