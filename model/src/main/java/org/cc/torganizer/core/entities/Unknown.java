package org.cc.torganizer.core.entities;

import java.util.Collections;
import java.util.Set;

/**
 * Wird verwendet, wenn die Gegner eines Matches noch nicht feststehen.
 */
public class Unknown extends Opponent {

  /**
   * Default.
   */
  public Unknown() {
    // gem. Bean-Spec.
  }

  /** {@inheritDoc} */
  @Override
  public Set<Player> getPlayers() {
    return Collections.emptySet();
  }

  @Override
  public OpponentType getOpponentType() {
    return null;
  }
}
