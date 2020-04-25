package org.cc.torganizer.frontend.disciplines.actions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.frontend.ApplicationMessages;

@RequestScoped
@Named
public class DeleteDiscipline extends DisciplinesAction {

  public static final String DISCIPLINES_I18N_BASE_NAME = "org.cc.torganizer.frontend.disciplines";

  @Inject
  private ApplicationMessages appMessages;

  /**
   * A club can only be deleted if it has no linked players.
   */
  public void execute() {
    Discipline discipline = state.getDiscipline();
    Long id = discipline.getId();

    boolean hasOpponents = !disciplinesRepository.getOpponents(id, 0, 1).isEmpty();

    if (hasOpponents) {
      appMessages.addMessage(DISCIPLINES_I18N_BASE_NAME, "no_delete_linked_players",
          new Object[]{discipline.getName()});
    } else {
      disciplinesRepository.delete(id);
      state.synchronize();
    }
  }
}
