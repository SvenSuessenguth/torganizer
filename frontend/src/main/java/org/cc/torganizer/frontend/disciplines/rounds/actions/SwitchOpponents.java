package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;
import org.cc.torganizer.persistence.GroupsRepository;

/**
 * Switch two opponents between groups and position.
 */
@Named
@RequestScoped
@Slf4j
public class SwitchOpponents {
  @Inject
  private DisciplineRoundState drState;
  private GroupsRepository groupsRepository;

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

    var sourceGroupId = Long.toString(drState.getSourceGroupId());
    var sourceOpponentId = Long.toString(drState.getSourceOpponentId());
    var targetGroupId = params.get("gId");
    var targetOpponentId = params.get("oId");

    var message = "switch ${sourceGroupId} / ${sourceOpponentId} with ${targetGroupId} / ${targetOpponentId}"
      .replace("${sourceGroupId}", sourceGroupId)
      .replace("${sourceOpponentId}", sourceOpponentId)
      .replace("${targetGroupId}", targetGroupId)
      .replace("${targetOpponentId}", targetOpponentId);

    //

    log.debug(message);
  }
}
