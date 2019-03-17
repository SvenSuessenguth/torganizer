package org.cc.torganizer.core;

import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cc.torganizer.core.entities.System;

/**
 * Factory class to find the {@link OpponentToGroupsAssigner} to a given {@link System}.
 */
@ApplicationScoped
public class OpponentToGroupsAssignerFactory {

  @Inject
  private Instance<OpponentToGroupsAssigner> assigners;

  /**
   * Gibt den zu dem System passenden OpponentToGroupsAssigner zurueck.
   *
   * @param system System
   * @return OpponentToGroupsAssigner
   */
  public OpponentToGroupsAssigner getOpponentToGroupsAssigner(System system) {

    for(OpponentToGroupsAssigner assigner : assigners){
      if(Objects.equals(system, assigner.getSystem())){
        return assigner;
      }
    }

    throw new MissingOpponentsToGroupsAssignerException(system);
  }
}
