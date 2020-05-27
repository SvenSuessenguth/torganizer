package org.cc.torganizer.frontend.tournaments;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIInput;
import javax.inject.Named;

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
