package org.cc.torganizer.frontend.tournaments;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class TournamentsController {

  @Inject
  private TournamentsState state;


  public void saveSelected() {
    System.out.println("save selected with name: '" + state.getCurrent().getName() + "'");
    
  }

  public void createNew() {
    System.out.println("create new with name: '" + state.getCurrent().getName() + "'");
  }

}
