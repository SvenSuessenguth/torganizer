package org.cc.torganizer.frontend.logging;

import java.io.Serializable;
import java.util.logging.Level;

public interface SimplifiedLoggerFacade extends Serializable {
  void severe(String msg);

  void info(String msg);

  void log(Level level, String msg, Object[] params);

  void log(Level level, String msg, Throwable thrown);
}
