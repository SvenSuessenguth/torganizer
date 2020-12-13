package org.cc.torganizer.frontend.logging;

import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import org.apache.logging.log4j.ThreadContext;

@WebFilter(urlPatterns = "/*")
public class OnlineExtendLoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      // https://logging.apache.org/log4j/2.x/manual/thread-context.html
      ThreadContext.put("requestId", UUID.randomUUID().toString());
      ThreadContext.put("ipAddress", request.getRemoteAddr());
      ThreadContext.put("hostName", request.getServerName());

      chain.doFilter(request, response);
    } finally {
      ThreadContext.clearAll();
    }
  }
}
