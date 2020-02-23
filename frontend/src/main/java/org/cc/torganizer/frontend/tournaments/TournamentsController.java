package org.cc.torganizer.frontend.tournaments;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.frontend.logging.SimplifiedLogger;
import org.cc.torganizer.frontend.logging.online.Online;

@Named
@RequestScoped
public class TournamentsController {

  @Inject
  @Online
  private SimplifiedLogger log;

  @Inject
  private TournamentsState state;

  public void saveSelected() {
    log.severe("save selected with name: '" + state.getCurrent().getName() + "'");
  }
}
