package org.cc.torganizer.frontend.disciplines.core;

import jakarta.enterprise.inject.Vetoed;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import org.cc.torganizer.core.entities.Discipline;

@Vetoed
@Data
public class DisciplinesCoreState implements Serializable {
  private Discipline discipline;
  private List<Discipline> disciplines;
}
