package org.cc.torganizer.frontend;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Map;
import org.apache.logging.log4j.Logger;

@Named
@ViewScoped
public class SwitchOpponent implements Serializable {

  @Inject
  private Logger logger;

  private Long sourceGroupId;
  private Long sourceOpponentId;

  public void setSwitchSource() {
    Map<String, String> params = FacesContext.getCurrentInstance().
        getExternalContext().getRequestParameterMap();
    sourceGroupId = Long.valueOf(params.get("gId"));
    sourceOpponentId = Long.valueOf(params.get("oId"));

    logger.info("setSwapSource: " + sourceGroupId + " / " + sourceOpponentId);
  }

  public void switchWith() {
    Map<String, String> params = FacesContext.getCurrentInstance().
        getExternalContext().getRequestParameterMap();
    Long targetGroupId = Long.valueOf(params.get("gId"));
    Long targetOpponentId = Long.valueOf(params.get("oId"));

    logger.info("swapWith: " + sourceGroupId + "/" + sourceOpponentId + " with " + targetGroupId + "/" + targetOpponentId);
  }
}
