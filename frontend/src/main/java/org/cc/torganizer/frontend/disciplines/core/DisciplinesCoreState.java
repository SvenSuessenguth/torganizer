package org.cc.torganizer.frontend.disciplines.core;

import jakarta.enterprise.inject.Vetoed;
import java.io.Serializable;
import java.util.List;
import org.cc.torganizer.core.entities.Discipline;

/**
 * State of the UI for editing Disciplines.
 */
@Vetoed
public class DisciplinesCoreState implements Serializable {

  private Discipline discipline;
  private List<Discipline> disciplines;

  public List<Discipline> getDisciplines() {
    return disciplines;
  }

  public Discipline getDiscipline() {
    return discipline;
  }

  public void setDiscipline(Discipline discipline) {
    this.discipline = discipline;
  }

  public void setDisciplines(List<Discipline> disciplines) {
    this.disciplines = disciplines;
  }
}
