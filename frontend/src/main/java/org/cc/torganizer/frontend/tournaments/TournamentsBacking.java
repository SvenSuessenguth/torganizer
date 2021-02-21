package org.cc.torganizer.frontend.tournaments;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIInput;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * Backing for Tournaments.
 */
@RequestScoped
@Named
public class TournamentsBacking {

  private UIInput nameInputText;

  @Inject
  private TournamentsState state;

  public TournamentsState getState() {
    return state;
  }

  public String getNameClientId() {
    return nameInputText.getClientId();
  }

  public UIInput getNameInputText() {
    return nameInputText;
  }

  public void setNameInputText(UIInput nameInputText) {
    this.nameInputText = nameInputText;
  }
}
