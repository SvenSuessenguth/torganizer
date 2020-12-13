package org.cc.torganizer.core;

import org.cc.torganizer.core.entities.System;

/**
 * This exception is thrown, when no OpponentsToGroupsAssigner is found.
 */
public class MissingOpponentsToGroupsAssignerException extends RuntimeException {

  public MissingOpponentsToGroupsAssignerException(System system) {
    super("No OpponentsToGroupsAssigner found for System '" + system + "'");
  }
}
