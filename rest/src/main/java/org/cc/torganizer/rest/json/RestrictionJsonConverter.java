package org.cc.torganizer.rest.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import org.cc.torganizer.core.entities.Restriction;

/**
 * What does a restriction-json look like:
 * <p>
 * AgeRestriction
 * <pre>
 * {
 *   id:
 *   type:
 *   maxDateOfBirth:
 *   minDateOfBirth:
 * }
 * </pre>
 *
 * @author svens
 */
@RequestScoped
public class RestrictionJsonConverter extends ModelJsonConverter<Restriction> {

  public RestrictionJsonConverter() {
  }

  @Override
  public JsonObject toJsonObject(Restriction restriction) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonObjectBuilder objectBuilder = factory.createObjectBuilder();

    add(objectBuilder, "id", restriction.getId());

    return objectBuilder.build();
  }

  @Override
  public JsonArray toJsonArray(Collection<Restriction> restrictions) {
    JsonBuilderFactory factory = Json.createBuilderFactory(new HashMap<>());
    final JsonArrayBuilder arrayBuilder = factory.createArrayBuilder();

    restrictions.forEach(restriction -> arrayBuilder.add(this.toJsonObject(restriction)));

    return arrayBuilder.build();
  }

  @Override
  public Restriction toModel(JsonObject jsonObject) {

    String discriminator = jsonObject.getString("discriminator");

    Restriction restriction = Restriction.Type.getByDiscriminator(discriminator).create();

    String idString = get(jsonObject, "id");
    Long id = idString == null ? null : Long.valueOf(idString);
    restriction.setId(id);

    return restriction;
  }

  @Override
  public Collection<Restriction> toModels(JsonArray jsonArray) {
    List<Restriction> restrictions = new ArrayList<>();

    jsonArray.forEach((JsonValue arrayValue) -> restrictions.add(toModel((JsonObject) arrayValue)));

    return restrictions;
  }
}
