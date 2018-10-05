package org.cc.torganizer.webapp;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Bean, in which Configuration is injected and can be used inside XHTML.
 */
@RequestScoped
@Named
public class Config {
  /**
   * Base-URL for accessing Rest-Services.
   */
  @Resource(lookup = "torganizer/resourcesUrl")
  private String resourcesUrl;

  public String getResourcesUrl() {
    return resourcesUrl;
  }
}