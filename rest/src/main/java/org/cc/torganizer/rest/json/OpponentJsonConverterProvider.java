package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Objects;

@RequestScoped
public class OpponentJsonConverterProvider{

  @Inject @Any
  private Instance<OpponentJsonConverter<?>> opponentConverters;

  public ModelJsonConverter<? extends Opponent> getConverter(OpponentType opponentType){
    for(OpponentJsonConverter<?> converter:opponentConverters){
      if(Objects.equals(opponentType, converter.getOpponentType())){
        return (ModelJsonConverter<? extends Opponent>)converter;
      }
    }
    return null;
  }

  public ModelJsonConverter<? extends Opponent> getConverter(Collection<Opponent> opponents){

    OpponentType opponentType;
    if(opponents.isEmpty()){
      return null;
    }
    else{
      Opponent opponent = opponents.iterator().next();
      opponentType = opponent.getOpponentType();
      return getConverter(opponentType);
    }
  }
}
