package org.cc.torganizer.frontend.disciplines;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;
import org.cc.torganizer.core.entities.OpponentTypeRestriction;
import org.cc.torganizer.frontend.ApplicationState;
import org.cc.torganizer.frontend.State;
import org.cc.torganizer.persistence.TournamentsRepository;

@ConversationScoped
@Named
public class DisciplinesState implements Serializable, State {

  private Discipline discipline;
  private List<Discipline> disciplines;
  private List<Opponent> assignableOpponents;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private ApplicationState applicationState;

  /**
   * Create new discipline and synchronizing with database.
   */
  @PostConstruct
  public void postConstruct() {
    // round is added on saving the discipline
    discipline = new Discipline();
    discipline.addRestriction(new GenderRestriction());
    discipline.addRestriction(new OpponentTypeRestriction());
    discipline.addRestriction(new AgeRestriction());

    synchronize();
  }

  @Override
  public void synchronize() {
    Long tournamentId = applicationState.getTournamentId();
    disciplines = tournamentsRepository.getDisciplines(tournamentId, 0, 1000);

    synchronizeOpponents();
  }

  /**
   * Synchronizing current discipline with database.
   */
  public void synchronizeOpponents() {
    Long tournamentId = applicationState.getTournamentId();
    assignableOpponents = tournamentsRepository.getAssignableOpponentsForDiscipline(tournamentId,
        discipline, 0, 1000);
  }

  public Gender[] getGenders() {
    return Gender.values();
  }

  public OpponentType[] getOpponentTypes() {
    return OpponentType.values();
  }

  public List<Discipline> getDisciplines() {
    return disciplines;
  }

  public Discipline getDiscipline() {
    return discipline;
  }

  public void setDiscipline(Discipline discipline) {
    this.discipline = discipline;
  }

  /**
   * Getting opponents that can be assigned to the current discipline.
   */
  public List<Opponent> getAssignableOpponents() {
    List<Opponent> result = new ArrayList<>(assignableOpponents);

    // remove opponents already added to discipline
    for (Opponent opponent : discipline.getOpponents()) {
      result.remove(opponent);
    }

    return result;
  }
}
