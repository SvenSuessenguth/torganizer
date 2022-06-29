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
    insertMappedDiagnosticContext(request);
    try {
      chain.doFilter(request, response);
    } finally {
      clearMappedDiagnosticContext();
    }
  }

  private void insertMappedDiagnosticContext(ServletRequest request) {
    MDC.put("correlationId", UUID.randomUUID().toString());
    MDC.put("remoteAddr", request.getRemoteAddr());
    MDC.put("serverName", request.getServerName());
  }

  private void clearMappedDiagnosticContext() {
    MDC.clear();
  }

}
