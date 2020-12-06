package org.cc.torganizer.frontend.logging;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RequestScoped
public class LoggerProducer {

  @Produces
  @Dependent
  public Logger produceLogger(InjectionPoint injectionPoint) {
    return LogManager.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
  }
}
