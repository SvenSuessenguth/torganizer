package org.cc.torganizer.core;

import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;

import org.cc.torganizer.core.entities.System;

/**
 * Factory class to find the {@link PendingMatchDetector} to a given {@link System}.
 */
@ApplicationScoped
public class PendingMatchDetectorFactory {

  private Instance<PendingMatchDetector> detectors;

  /**
   * Gibt den zu dem System passenden PendingMatchDetector zurueck.
   *
   * @param system System
   * @return PendingMatchDetector
   */
  public PendingMatchDetector getPendingMatchDetector(System system) {

    for (PendingMatchDetector detector : detectors) {
      if (Objects.equals(system, detector.getSystem())) {
        return detector;
      }
    }

    throw new MissingPendingMatchDetectorException(system);

  }
}
