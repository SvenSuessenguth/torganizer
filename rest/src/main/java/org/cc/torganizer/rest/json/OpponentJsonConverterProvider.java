package org.cc.torganizer.rest.json;

import java.util.Collection;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;

@RequestScoped
public class OpponentJsonConverterProvider {

  @Inject
  private Instance<OpponentJsonConverter> opponentConverters;

  @SuppressWarnings("rawtypes")
  public ModelJsonConverter getConverter(OpponentType opponentType) {
    for (OpponentJsonConverter converter : opponentConverters) {
      if (Objects.equals(opponentType, converter.getOpponentType())) {
        return (ModelJsonConverter) converter;
      }
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  public ModelJsonConverter getConverter(Collection<Opponent> opponents) {

    OpponentType opponentType;
    if (opponents.isEmpty()) {
      return null;
    } else {
      Opponent opponent = opponents.iterator().next();
      opponentType = opponent.getOpponentType();
      return getConverter(opponentType);
    }
  }
}
