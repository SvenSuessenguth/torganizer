package org.cc.torganizer.frontend.logging.online;

import static java.lang.reflect.Proxy.newProxyInstance;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.cc.torganizer.frontend.logging.SimplifiedLoggerFacade;
import org.cc.torganizer.frontend.logging.SimplifiedLoggerInvocationHandler;

@RequestScoped
public class OnlineLoggerProducer {

  /**
   * producing a simplified facade for a JUL-Logger.
   * @param injectionPoint information about the injectionPoint to create a JUL-logger
   *                       with the right name
   */
  @Produces
  @Online
  @Dependent
  public SimplifiedLoggerFacade produceOnlineLogger(InjectionPoint injectionPoint) {
    Class<?> declaringClass = injectionPoint.getMember().getDeclaringClass();
    String name = declaringClass.getName();

    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name, null);
    Class<?>[] loggerInterfaces = new Class[]{SimplifiedLoggerFacade.class};
    ClassLoader classLoader = this.getClass().getClassLoader();
    SimplifiedLoggerInvocationHandler handler = new SimplifiedLoggerInvocationHandler(logger);

    return (SimplifiedLoggerFacade) newProxyInstance(classLoader, loggerInterfaces, handler);
  }
}