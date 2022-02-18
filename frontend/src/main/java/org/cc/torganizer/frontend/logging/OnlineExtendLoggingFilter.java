package org.cc.torganizer.frontend.logging;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;

/**
 * Filter for setting ThreadContext-Parameter to the Logging framework.
 */
@WebFilter(urlPatterns = "/*")
public class OnlineExtendLoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      // https://logging.apache.org/log4j/2.x/manual/thread-context.html
      MDC.put("requestId", UUID.randomUUID().toString());
      MDC.put("ipAddress", request.getRemoteAddr());
      MDC.put("hostName", request.getServerName());
      MDC.put("name", "Sven");

      chain.doFilter(request, response);
    } finally {
      MDC.clear();
    }
  }
}
