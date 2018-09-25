package org.cc.torganizer.core.system;

import org.cc.torganizer.core.entities.Group;
import org.cc.torganizer.core.entities.System;

/**
 * Factory class to find the {@link PendingMatchDetector} to a given {@link System}.
 */
public class PendingMatchDetectorFactory {

  /**
   * Default.
   */
  public PendingMatchDetectorFactory() {
    // gem. Bean-Spec.
  }

  /**
   * Gibt den zu dem System passenden PendingMatchDetector zurueck.
   *
   * @param system System
   * @param group  Group
   * @return PendingMatchDetector
   */
  public PendingMatchDetector getPendingMatchDetector(System system, Group group) {
    if (system == null) {
      throw new IllegalArgumentException("system can not be null");
    }

    PendingMatchDetector pendingMatchDetector;

    switch (system) {
      case ROUND_ROBIN:
        pendingMatchDetector = new RoundRobinMatchDetector(group);
        break;
      case DOUBLE_ELIMINATION:
        pendingMatchDetector = new DoubleEliminationMatchDetector(group);
        break;
      case SINGLE_ELIMINATION:
        pendingMatchDetector = new SingleEliminationMatchDetector(group);
        break;
      default:
        pendingMatchDetector = null;
    }

    return pendingMatchDetector;
  }
}
