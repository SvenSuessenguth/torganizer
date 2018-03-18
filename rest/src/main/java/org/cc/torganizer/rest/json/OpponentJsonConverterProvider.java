package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.OpponentType;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Collection;
import java.util.Objects;

@RequestScoped
public class OpponentJsonConverterProvider{

  @Inject @Any
  private Instance<OpponentJsonConverter> opponentConverters;

  public ModelJsonConverter getConverter(OpponentType opponentType){
    for(OpponentJsonConverter converter:opponentConverters){
      if(Objects.equals(opponentType, converter.getOpponentType())){
        return (ModelJsonConverter)converter;
      }
    }
    return null;
  }
}
