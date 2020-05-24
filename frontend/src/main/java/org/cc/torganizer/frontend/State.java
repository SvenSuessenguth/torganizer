package org.cc.torganizer.frontend;

public interface State {

  /**
   * synchronizing the cached data in the state with actual data from the dbms.
   */
  public abstract void synchronize();
}
