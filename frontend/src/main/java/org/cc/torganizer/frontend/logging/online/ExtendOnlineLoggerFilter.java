package org.cc.torganizer.frontend.logging.online;

import com.ibm.websphere.logging.hpel.LogRecordContext;
import java.io.IOException;
import java.util.UUID;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import org.cc.torganizer.frontend.logging.SimplifiedLogger;

@WebFilter(urlPatterns = "/*")
public class ExtendOnlineLoggerFilter implements Filter {


  @Inject
  @Online
  private SimplifiedLogger log;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    // https://openliberty.io/blog/2019/12/03/custom-fields-json-logs.html
    // Neither 'extensionName' nor 'extensionValue' parameter can be null
    String requestId = UUID.randomUUID().toString();
    LogRecordContext.addExtension("request", requestId);

    log.info("logging extended with request.id '" + requestId + "'");

    chain.doFilter(request, response);
  }
}
