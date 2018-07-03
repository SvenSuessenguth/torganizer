package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;

public abstract class OpponentJsonConverter<T extends Opponent> extends ModelJsonConverter<T>{

  public abstract OpponentType getOpponentType();

}
