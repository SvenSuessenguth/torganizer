package org.cc.torganizer.frontend.logging;

import java.lang.reflect.Method;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReducedLoggerTest {

  @Test
  public void testMethodenAusInterfaceInLoggerVorhanden() {
    Class<SimplifiedLogger> reducedLoggerClass = SimplifiedLogger.class;
    Method[] reducedLoggerMethods = reducedLoggerClass.getMethods();

    Class<java.util.logging.Logger> loggerClass = java.util.logging.Logger.class;

    for (Method reducedLoggerMethod : reducedLoggerMethods) {
      String name = reducedLoggerMethod.getName();
      Class<?>[] parameterTypes = reducedLoggerMethod.getParameterTypes();
      Method loggerMethod = null;

      try {
        loggerMethod = loggerClass.getMethod(name, parameterTypes);
      } catch (NoSuchMethodException e) {
        Assertions.fail("Die Methode '" + name + "' ist im Interface vorgegeben, existiert aber nicht im Logger");
      }

      Assertions.assertThat(loggerMethod).isNotNull();
    }
  }
}