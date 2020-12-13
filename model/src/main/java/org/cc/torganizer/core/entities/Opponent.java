package org.cc.torganizer.core.entities;

import static java.time.LocalDateTime.MIN;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalDateTime;
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

  /**
   * Calculating the average idle-time of all players in minutes. This is used to minimize the waiting time between
   * matches.
   */
  public Long getIdleTime() {
    long overallMinutesSinceLastMatch = 0L;

    for (Player p : getPlayers()) {
      LocalDateTime lastMatchTime = p.getLastMatchTime();

      lastMatchTime = lastMatchTime == null ? MIN : lastMatchTime;

      overallMinutesSinceLastMatch += MINUTES.between(lastMatchTime, now());
    }

    int playerCount = getPlayers().size();
    return playerCount != 0 ? overallMinutesSinceLastMatch / playerCount : Long.MAX_VALUE;
  }
}
