package org.cc.torganizer.frontend.tournaments;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIInput;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TournamentsBacking {

  private UIInput nameInputText;

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
