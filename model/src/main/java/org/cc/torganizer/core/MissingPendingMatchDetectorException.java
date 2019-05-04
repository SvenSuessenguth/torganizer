package org.cc.torganizer.core;

import org.cc.torganizer.core.entities.System;

class MissingPendingMatchDetectorException extends RuntimeException {

  MissingPendingMatchDetectorException(System system) {
    super("No PendingMatchDetector found for System '" + system + "'");
  }
}
