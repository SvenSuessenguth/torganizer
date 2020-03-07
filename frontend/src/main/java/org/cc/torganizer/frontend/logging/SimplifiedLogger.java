package org.cc.torganizer.frontend.logging;

import java.io.Serializable;

public interface SimplifiedLogger extends Serializable {
  void severe(String msg);

  void info(String msg);
}
