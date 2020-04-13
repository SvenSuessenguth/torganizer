package org.cc.torganizer.frontend;

import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Tournament;

@ConversationScoped
@Named
public class ApplicationState implements Serializable {

  private Tournament current;

  public Tournament getCurrent() {
    return current;
  }

  public void setCurrent(Tournament current) {
    this.current = current;
  }

}
