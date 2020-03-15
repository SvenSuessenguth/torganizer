package org.cc.torganizer.frontend.players;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.logging.log4j.Logger;

@Named
@RequestScoped
public class PlayersController {

  @Inject
  private Logger logger;

  public void save() {
    logger.info("save player");
  }
}
