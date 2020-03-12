package org.cc.torganizer.frontend.logging;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
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
