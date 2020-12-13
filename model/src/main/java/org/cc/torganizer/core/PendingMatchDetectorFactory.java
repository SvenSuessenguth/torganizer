package org.cc.torganizer.core;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import java.util.Objects;
import org.cc.torganizer.core.entities.System;

/**
 * Factory class to find the {@link PendingMatchDetector} to a given {@link System}.
 */
@ApplicationScoped
class PendingMatchDetectorFactory {

  @Inject
  private Instance<PendingMatchDetector> detectors;

  /**
   * Gibt den zu dem System passenden PendingMatchDetector zurueck.
   */
  PendingMatchDetector getPendingMatchDetector(System system) {

    for (PendingMatchDetector detector : detectors) {
      if (Objects.equals(system, detector.getSystem())) {
        return detector;
      }
    }

    throw new MissingPendingMatchDetectorException(system);

  }
}
