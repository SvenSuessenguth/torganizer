package org.cc.torganizer.core.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

import static java.time.LocalDateTime.MIN;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Abstract Opponent class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class Opponent extends Entity {

  private Status status = Status.ACTIVE;

  public abstract Collection<Player> getPlayers();

  public abstract OpponentType getOpponentType();

  /**
   * Calculating the average idle-time of all players in minutes. This is used to minimize the
   * waiting time between matches.
   */
  public Long getIdleTime() {
    var overallMinutesSinceLastMatch = 0L;

    for (var p : getPlayers()) {
      var lastMatchTime = p.getLastMatchTime();
      lastMatchTime = lastMatchTime == null ? MIN : lastMatchTime;
      overallMinutesSinceLastMatch += MINUTES.between(lastMatchTime, now());
    }

    var playerCount = getPlayers().size();
    return playerCount != 0 ? overallMinutesSinceLastMatch / playerCount : Long.MAX_VALUE;
  }
}
