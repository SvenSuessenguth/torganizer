package org.cc.torganizer.frontend.squads;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Squad;

@ViewScoped
@Named
public class SquadsState implements Serializable {

  private Squad current;

  public Squad getCurrent() {
    return current;
  }

  public void setCurrent(Squad current) {
    this.current = current;
  }
}
