package org.cc.torganizer.frontend.logging;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
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
