package org.cc.torganizer.core.entities;

import java.util.Collection;

/**
 * Abstract Opponent class.
 */
public abstract class Opponent extends Entity {

  private Status status = Status.ACTIVE;

  public abstract Collection<Player> getPlayers();

  public abstract OpponentType getOpponentType();

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status inStatus) {
    this.status = inStatus;
  }
}
