package org.cc.torganizer.core.system;

import org.cc.torganizer.core.entities.System;

public class MissingPendingMatchDetectorException extends RuntimeException {

  public MissingPendingMatchDetectorException(System system){
    super("No PendingMatchDetector found for System '"+system+"'");
  }
}
