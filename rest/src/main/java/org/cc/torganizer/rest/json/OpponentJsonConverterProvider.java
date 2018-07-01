package org.cc.torganizer.rest.json;

import java.util.Collection;
import java.util.Objects;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;

@RequestScoped
public class OpponentJsonConverterProvider{

  @Inject @OpponentJsonConverter
  private Instance<ModelJsonConverter<?>> opponentConverters;

  public ModelJsonConverter<?> getConverter(OpponentType opponentType){
    for(ModelJsonConverter<?>converter:opponentConverters){
      
      Class<? extends ModelJsonConverter> c = converter.getClass();
      OpponentJsonConverter a = c.getAnnotation(OpponentJsonConverter.class);
      OpponentType convertersOpponentType = a.type();
      
      if(Objects.equals(opponentType, convertersOpponentType)){
        return converter;
      }
    }
    return null;
  }

  public ModelJsonConverter<?> getConverter(Collection<Opponent> opponents){

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
