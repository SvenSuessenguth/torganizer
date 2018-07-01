package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;

public interface OpponentJsonConverter<T extends Opponent> {

  public abstract OpponentType getOpponentType();
}
