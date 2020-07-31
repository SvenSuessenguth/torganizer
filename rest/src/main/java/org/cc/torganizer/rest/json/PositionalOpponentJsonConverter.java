package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Opponent;
import org.cc.torganizer.core.entities.PositionalOpponent;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

@RequestScoped
public class PositionalOpponentJsonConverter extends BaseModelJsonConverter<PositionalOpponent> {

  @Inject
  private OpponentJsonConverterProvider provider;

  @Override
  public JsonObject toJsonObject(PositionalOpponent positionalOpponent) {
    ModelJsonConverter<Opponent> converter = getConverter(positionalOpponent);

    JsonObject jsonObject = converter.toJsonObject(positionalOpponent.getOpponent());

    JsonPatch patch = Json.createPatchBuilder()
      .add("/position", positionalOpponent.getPosition())
      .build();

    return patch.apply(jsonObject);
  }

  @Override
  public JsonArray toJsonArray(Collection<PositionalOpponent> positionalOpponents) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    positionalOpponents.forEach(po -> arrayBuilder.add(this.toJsonObject(po)));

    return arrayBuilder.build();
  }

  @Override
  public PositionalOpponent toModel(JsonObject jsonObject, PositionalOpponent model) {
    return null;
  }

  @Override
  public Collection<PositionalOpponent> toModels(JsonArray jsonArray,
                                                 Collection<PositionalOpponent> models) {
    return Collections.emptyList();
  }

  protected ModelJsonConverter<Opponent> getConverter(PositionalOpponent positionalOpponent) {
    if (positionalOpponent == null) {
      return null;
    }

    return provider.getConverter(positionalOpponent.getOpponent().getOpponentType());
  }
}
