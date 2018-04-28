package org.cc.torganizer.rest.json;

import org.cc.torganizer.core.entities.Round;

import javax.enterprise.context.RequestScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.util.Collection;

/**
 * A json-disciplines contains nothing but id, name and restrictions.
 *
 */
@RequestScoped
public class RoundJsonConverter extends ModelJsonConverter<Round> {

  public RoundJsonConverter() {
  }

  @Override
  public JsonObject toJsonObject(Round round) {
    return null;
  }

  @Override
  public JsonArray toJsonArray(Collection<Round> rounds) {
    return null;
  }

  @Override
  public Round toModel(JsonObject jsonObject, Round round) {
    return null;
  }

  @Override
  public Collection<Round> toModels(JsonArray jsonArray, Collection<Round> rounds) {
    return null;
  }
}
