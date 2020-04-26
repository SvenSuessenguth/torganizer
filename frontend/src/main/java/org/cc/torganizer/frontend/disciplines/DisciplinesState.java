package org.cc.torganizer.frontend.disciplines;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
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

@ViewScoped
@Named
public class DisciplinesState extends State implements Serializable {

  private Discipline discipline;
  private List<Discipline> disciplines;
  private List<Opponent> assignableOpponents;

  @Inject
  private TournamentsRepository tournamentsRepository;

  @Inject
  private ApplicationState applicationState;

  @PostConstruct
  public void postConstruct() {
    synchronize();
  }

  @Override
  public void synchronize() {
    discipline = new Discipline();
    discipline.addRestriction(new GenderRestriction());
    discipline.addRestriction(new OpponentTypeRestriction());
    discipline.addRestriction(new AgeRestriction());

    Long tournamentId = applicationState.getTournamentId();
    disciplines = tournamentsRepository.getDisciplines(tournamentId, 0, 1000);

    synchronizeOpponents();
  }

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

  public List<Opponent> getAssignableOpponents() {
    return assignableOpponents;
  }
}
