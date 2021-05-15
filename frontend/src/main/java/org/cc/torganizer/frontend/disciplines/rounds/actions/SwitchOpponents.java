package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;
import org.cc.torganizer.persistence.GroupsRepository;

@Named
@RequestScoped
public class SwitchOpponents {

  @Inject
  private DisciplineRoundState roundState;

  @Inject
  private Logger logger;

  @Inject
  private GroupsRepository groupsRepository;

  public void setSwitchSource() {
    Map<String, String> params = FacesContext.getCurrentInstance()
        .getExternalContext().getRequestParameterMap();
    var sourceGroupId = Long.valueOf(params.get("gId"));
    var sourceOpponentId = Long.valueOf(params.get("oId"));

    roundState.setSourceGroupId(sourceGroupId);
    roundState.setSourceOpponentId(sourceOpponentId);
  }

  public void switchWith() {
    Map<String, String> params = FacesContext.getCurrentInstance().
        getExternalContext().getRequestParameterMap();
    var targetGroupId = Long.valueOf(params.get("gId"));
    var targetOpponentId = Long.valueOf(params.get("oId"));

    String message = """
        switch %s / %s
        with %s / %s
        """
        .formatted(roundState.getSourceGroupId(), roundState.getSourceOpponentId(), targetGroupId, targetOpponentId);
    logger.debug(message);
  }
}
