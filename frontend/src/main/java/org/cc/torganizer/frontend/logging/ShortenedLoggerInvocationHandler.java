package org.cc.torganizer.frontend.logging;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ShortenedLoggerInvocationHandler implements InvocationHandler {

  private Logger logger;

  public ShortenedLoggerInvocationHandler(Logger logger) {
    this.logger = logger;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Class[] argsClasses = new Class[args.length];
    for (int i = 0; i < args.length; i++) {
      argsClasses[i] = args[i].getClass();
    }

    Method loggerMethod = logger.getClass().getMethod(method.getName(), argsClasses);
    return loggerMethod.invoke(logger, args);
  }
}
