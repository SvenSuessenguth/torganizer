package org.cc.torganizer.frontend.players.actions;

import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;

@RequestScoped
@Named
public class OrderPlayers extends Action {

  @SuppressWarnings("unused")
  public void execute(AjaxBehaviorEvent event) {
    playersService.orderPlayers();
  }
}
