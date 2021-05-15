package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;

@Named
@RequestScoped
public class SwitchOpponents {

  @Inject
  private DisciplineRoundState disciplineRoundState;

  @Inject
  private Logger logger;

  /**
   * Saveing the Opponent to switch given in the request.
   */
  public void setSwitchSource() {
    Map<String, String> params = FacesContext.getCurrentInstance()
        .getExternalContext().getRequestParameterMap();
    var sourceGroupId = Long.valueOf(params.get("gId"));
    var sourceOpponentId = Long.valueOf(params.get("oId"));

    disciplineRoundState.setSourceGroupId(sourceGroupId);
    disciplineRoundState.setSourceOpponentId(sourceOpponentId);
  }

  /**
   * Switch the source-opponent with the opponent given in the request.
   */
  public void switchWith() {
    Map<String, String> params = FacesContext.getCurrentInstance()
        .getExternalContext().getRequestParameterMap();
    var targetGroupId = Long.valueOf(params.get("gId"));
    var targetOpponentId = Long.valueOf(params.get("oId"));

    String message = """
        switch %s / %s
        with %s / %s
        """
        .formatted(disciplineRoundState.getSourceGroupId(), disciplineRoundState.getSourceOpponentId(), targetGroupId, targetOpponentId);
    logger.debug(message);
  }
}
