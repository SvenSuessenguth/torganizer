package org.cc.torganizer.rest.json;

import java.util.Collection;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonPatch;

import org.cc.torganizer.core.entities.PositionalOpponent;

@RequestScoped
public class PositionalOpponentJsonConverter extends BaseModelJsonConverter<PositionalOpponent> {

  @Inject
  private OpponentJsonConverterProvider provider;

  @Override
  public JsonObject toJsonObject(PositionalOpponent positionalOpponent) {
    ModelJsonConverter converter = getConverter(positionalOpponent);

    JsonObject jsonObject = converter.toJsonObject(positionalOpponent.getOpponent());

    JsonPatch patch = Json.createPatchBuilder().
        add("/position", positionalOpponent.getPosition()).
        build();
    JsonObject result = patch.apply(jsonObject);

    return result;
  }

  @Override
  public JsonArray toJsonArray(Collection<PositionalOpponent> positionalOpponents) {
    return null;
  }

  @Override
  public PositionalOpponent toModel(JsonObject jsonObject, PositionalOpponent model) {
    return null;
  }

  @Override
  public Collection<PositionalOpponent> toModels(JsonArray jsonArray, Collection<PositionalOpponent> models) {
    return null;
  }


  protected ModelJsonConverter getConverter(Collection<PositionalOpponent> pOpponents) {
    if (pOpponents == null || pOpponents.isEmpty()) {
      return null;
    }

    return getConverter(pOpponents.iterator().next());
  }

  protected ModelJsonConverter getConverter(PositionalOpponent pOppponent) {
    if (pOppponent == null) {
      return null;
    }

    return (ModelJsonConverter) provider.getConverter(pOppponent.getOpponent().getOpponentType());
  }
}