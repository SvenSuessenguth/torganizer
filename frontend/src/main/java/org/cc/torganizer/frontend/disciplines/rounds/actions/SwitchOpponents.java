package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;
import org.slf4j.Logger;

/**
 * Switch two opponents between groups and position.
 */
@Named
@RequestScoped
public class SwitchOpponents {

  @Inject
  private DisciplineRoundState drState;
  @Inject
  private Logger logger;

  /**
   * Saveing the Opponent to switch given in the request.
   */
  public void setSwitchSource() {
    var params = FacesContext.getCurrentInstance()
      .getExternalContext().getRequestParameterMap();
    var sourceGroupId = Long.valueOf(params.get("gId"));
    var sourceOpponentId = Long.valueOf(params.get("oId"));

    drState.setSourceGroupId(sourceGroupId);
    drState.setSourceOpponentId(sourceOpponentId);
  }

  /**
   * Switch the source-opponent with the opponent given in the request.
   */
  public void switchWith() {
    var params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

    var message = "switch ${sourceGroupId} / ${sourceOpponentId} with ${targetGroupId} / ${targetOpponentId}"
      .replace("${sourceGroupId}", Long.toString(drState.getSourceGroupId()))
      .replace("${sourceOpponentId}", Long.toString(drState.getSourceOpponentId()))
      .replace("${targetGroupId}", params.get("gId"))
      .replace("${targetOpponentId}", params.get("oId"));

    logger.debug(message);
  }
}
