package org.cc.torganizer.frontend.disciplines.core;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import org.cc.torganizer.core.entities.AgeRestriction;
import org.cc.torganizer.core.entities.Discipline;
import org.cc.torganizer.core.entities.Gender;
import org.cc.torganizer.core.entities.GenderRestriction;
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

}
