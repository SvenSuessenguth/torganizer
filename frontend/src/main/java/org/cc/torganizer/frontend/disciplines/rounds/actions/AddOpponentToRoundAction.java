package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.HashSet;
import org.cc.torganizer.core.OpponentsToGroupsAssigner;
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

  /**
   * Adding an opponent to the current round.
   *
   * @param opponent opponent to add to the round
   */
  public void execute(Opponent opponent) {
    var round = roundState.getRound();
    var groups = round.getGroups();

    var opponents = new HashSet<Opponent>();
    opponents.add(opponent);
    assigner.assign(opponents, groups);

    roundsRepository.update(round);
    synchronizer.synchronize(roundState);

    // reset to previous selected round
    roundState.setRound(round);
  }
}
