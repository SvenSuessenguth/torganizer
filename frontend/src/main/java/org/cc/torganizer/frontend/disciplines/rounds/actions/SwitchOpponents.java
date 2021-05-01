package org.cc.torganizer.frontend.disciplines.rounds.actions;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.Map;
import org.cc.torganizer.frontend.disciplines.rounds.DisciplineRoundState;
import org.cc.torganizer.persistence.GroupsRepository;

@Named
@RequestScoped
public class SwitchOpponents {

  @Inject
  private DisciplineRoundState roundState;

  @Inject
  private GroupsRepository groupsRepository;

  public void setSwitchSource() {
    Map<String, String> params = FacesContext.getCurrentInstance().
        getExternalContext().getRequestParameterMap();
    Long sourceGroupId = Long.valueOf(params.get("gId"));
    Long sourceOpponentId = Long.valueOf(params.get("oId"));

    roundState.setSourceGroupId(sourceGroupId);
    roundState.setSourceOpponentId(sourceOpponentId);
  }

  public void switchWith() {
    Map<String, String> params = FacesContext.getCurrentInstance().
        getExternalContext().getRequestParameterMap();
    Long targetGroupId = Long.valueOf(params.get("gId"));
    Long targetOpponentId = Long.valueOf(params.get("oId"));

    System.out.println("switch " + roundState.getSourceGroupId() + "/" + roundState.getSourceOpponentId() + " with " + targetGroupId + "/" + targetOpponentId);
  }
}
