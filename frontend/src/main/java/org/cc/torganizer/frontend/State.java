package org.cc.torganizer.frontend;

public abstract class State {

  /**
   * synchronizing the cahed data in the state with actual data from the dbms.
   */
  public abstract void synchronize();
}
