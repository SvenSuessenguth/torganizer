package org.cc.torganizer.frontend.clubs.actions;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Club;

@RequestScoped
@Named
public class DeleteClub extends ClubsAction {

  @Inject
  private InitClubsState initClubsState;

  @Inject
  private FacesContext facesContext;

  /**
   * A club can only be deleted if it has no linked players.
   */
  public void execute() {
    Club current = state.getCurrent();
    Long id = current.getId();

    Long count = clubsRepository.countPlayers(current);

    if (count > 0) {
      FacesMessage message = new FacesMessage("Der Verein " + current.getName() + " kann nicht gelöscht werden, da noch Mitglieder verknüpft sind.");
      facesContext.addMessage(null, message);
    }else {
      clubsRepository.delete(id);
      initClubsState.execute();
    }
  }
}
