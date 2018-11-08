package org.cc.torganizer.webapp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Bean, in which Configuration is injected and can be used inside XHTML.
 */
@ApplicationScoped
@Named
public class Configurations {

  @Inject
  @ConfigProperty(name="resources.url")
  private String resourcesUrl;

  public String getResourcesUrl() {
    return resourcesUrl;
  }
}