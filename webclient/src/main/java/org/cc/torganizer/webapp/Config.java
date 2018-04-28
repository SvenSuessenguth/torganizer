package org.cc.torganizer.webapp;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class Config{
  @Resource(lookup = "torganizer/resourcesUrl")
  private String resourcesUrl;

  public String getResourcesUrl(){
    return resourcesUrl; }
}