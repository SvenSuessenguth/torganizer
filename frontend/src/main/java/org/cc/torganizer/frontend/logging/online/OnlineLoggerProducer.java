package org.cc.torganizer.frontend.logging.online;

import java.lang.reflect.Proxy;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.cc.torganizer.frontend.logging.ShortenedLoggerInvocationHandler;
import org.cc.torganizer.frontend.logging.SimplifiedLogger;

@RequestScoped
public class OnlineLoggerProducer {

  @Produces
  @Online
  @Dependent
  public SimplifiedLogger produceOnlineLogger(InjectionPoint injectionPoint) {
    Class<?> declaringClass = injectionPoint.getMember().getDeclaringClass();
    String name = declaringClass.getName();

    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name, null);
    Class<?>[] loggerInterfaces = new Class[]{SimplifiedLogger.class};
    ClassLoader classLoader = this.getClass().getClassLoader();
    ShortenedLoggerInvocationHandler loggerInvocationHandler = new ShortenedLoggerInvocationHandler(logger);

    return (SimplifiedLogger) Proxy.newProxyInstance(classLoader, loggerInterfaces, loggerInvocationHandler);
  }
}