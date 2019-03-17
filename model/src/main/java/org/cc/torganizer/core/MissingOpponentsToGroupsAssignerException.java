package org.cc.torganizer.core;

import org.cc.torganizer.core.entities.System;

public class MissingOpponentsToGroupsAssignerException extends RuntimeException {

  public MissingOpponentsToGroupsAssignerException(System system){
    super("No OpponentsToGroupsAssigner found for System '"+system+"'");
  }
}
